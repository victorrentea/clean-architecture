#!/usr/bin/env python3
"""Sonar-style Cognitive Complexity for Spring Framework Java codebase.

Implements the algorithm from https://www.sonarsource.com/docs/CognitiveComplexity.pdf
using tree-sitter-java.
"""
from __future__ import annotations

import os
import sys
from pathlib import Path
from typing import Optional

import subprocess
# --- vendored tree-sitter discovery (parameterized) ---
_here = os.path.dirname(os.path.abspath(__file__))
for _p in (os.environ.get("HEATMAP_PYLIBS"), os.path.join(_here, ".pylibs")):
    if _p and os.path.isdir(_p):
        sys.path.insert(0, _p)
import tree_sitter_java as tsj
from tree_sitter import Language, Parser

# Load language; handle both new and old binding signatures.
try:
    JAVA = Language(tsj.language())
except TypeError:
    JAVA = Language(tsj.language(), "java")

parser = Parser(JAVA)

def _git_root(start):
    try:
        return subprocess.check_output(["git", "-C", start, "rev-parse", "--show-toplevel"],
                                       text=True, stderr=subprocess.DEVNULL).strip()
    except Exception:
        return start
REPO = Path(os.environ.get("HEATMAP_REPO") or _git_root(_here)).resolve()
OUT_DIR = Path(os.environ.get("HEATMAP_OUT") or str(REPO)).resolve()
OUT_DIR.mkdir(parents=True, exist_ok=True)
EXTRA_PRUNE = {d for d in os.environ.get("HEATMAP_PRUNE", "").split(",") if d}

# ---------------------------------------------------------------------------
# Tree helpers
# ---------------------------------------------------------------------------

CLASS_LIKE = {
    "class_declaration",
    "interface_declaration",
    "enum_declaration",
    "record_declaration",
    "annotation_type_declaration",
}

METHOD_LIKE = {"method_declaration", "constructor_declaration", "compact_constructor_declaration"}


def node_text(node, src: bytes) -> str:
    return src[node.start_byte : node.end_byte].decode("utf-8", errors="replace")


def find_child_by_field(node, field: str):
    return node.child_by_field_name(field)


def get_name(node, src: bytes) -> Optional[str]:
    n = node.child_by_field_name("name")
    if n is not None:
        return node_text(n, src)
    # fallback search
    for c in node.children:
        if c.type == "identifier":
            return node_text(c, src)
    return None


# ---------------------------------------------------------------------------
# Cognitive complexity computation
# ---------------------------------------------------------------------------

# Nodes that introduce a nesting increment (+1 + current_nesting) AND increase
# nesting level when entering their body.
NESTING_INCREMENT_NODES = {
    "if_statement",          # handled specially for else-if
    "ternary_expression",
    "switch_expression",
    "switch_statement",
    "for_statement",
    "enhanced_for_statement",
    "while_statement",
    "do_statement",
    "catch_clause",
}


def boolean_groups(expr, src: bytes) -> int:
    """Count Sonar 'distinct contiguous runs' of && / || beyond the first.

    Implementation: linearise the boolean-expression tree into a flat sequence
    of operators by walking binary_expressions with operator && or ||, then
    count operator-changes. For an expression with N operators using K groups
    we add K (the spec example a && b && c -> 1 means 1 group total).

    Reading the whitepaper again: "For sequences of binary logical operators,
    reward concision by assigning a fundamental complexity of 1, regardless of
    how many you use. Then add another fundamental complexity increment for
    each subsequent change in operator."  So total = 1 + (changes).
    Examples:
        a && b && c        -> ops [&&, &&]     -> 1 + 0 = 1
        a && b || c        -> ops [&&, ||]     -> 1 + 1 = 2
        a && b || c && d   -> ops [&&, ||, &&] -> 1 + 2 = 3
    """
    ops: list[str] = []

    def collect(n):
        if n.type == "binary_expression":
            op_node = n.child_by_field_name("operator")
            op = node_text(op_node, src) if op_node is not None else ""
            if op in ("&&", "||"):
                left = n.child_by_field_name("left")
                right = n.child_by_field_name("right")
                if left is not None:
                    collect(left)
                ops.append(op)
                if right is not None:
                    collect(right)
                return
        elif n.type == "parenthesized_expression":
            for c in n.children:
                if c.is_named:
                    collect(c)
            return
        # other expression types -> stop; they are not part of the logical chain
        return

    collect(expr)
    if not ops:
        return 0
    groups = 1
    for i in range(1, len(ops)):
        if ops[i] != ops[i - 1]:
            groups += 1
    return groups


def is_top_level_boolean(expr) -> bool:
    """Check whether this binary_expression with && or || is the top of a chain.

    We avoid double-counting by only triggering the boolean-group counter at the
    topmost && / || in a chain. A node is top-level if its parent is not itself
    a binary_expression with && or ||, modulo parenthesized_expression wrappers.
    """
    p = expr.parent
    while p is not None and p.type == "parenthesized_expression":
        p = p.parent
    if p is None:
        return True
    if p.type == "binary_expression":
        # If parent op is && or ||, then we're not the top.
        op_node = p.child_by_field_name("operator")
        op = ""
        if op_node is not None:
            # cheap textual peek -- we don't have src here; use type fallback
            # by reading node's bytes via global SRC (set in compute_method).
            op = SRC[op_node.start_byte : op_node.end_byte].decode("utf-8", errors="replace")
        if op in ("&&", "||"):
            return False
    return True


# Global source bytes for current file (used by is_top_level_boolean to avoid
# threading src through every recursion). Set per-file.
SRC: bytes = b""


def compute_method_complexity(method_node, enclosing_method_name: Optional[str]) -> int:
    """Compute cognitive complexity of a method/constructor body.

    Walks the body. Lambdas/nested method bodies increase nesting but their
    complexity contributes to this method (lambdas) or are skipped entirely
    (nested method declarations belong to the nested class' own scoring).
    """
    body = method_node.child_by_field_name("body")
    if body is None:
        return 0

    total = 0

    def walk(node, nesting: int, inside_nested_decl: bool = False):
        """Recursive walker.

        nesting: current nesting level (0 inside the immediate method body).
        inside_nested_decl: True if we've crossed into a nested class/method
            declaration whose contents must NOT contribute to this method's
            score (but lambdas DO contribute).
        """
        nonlocal total
        t = node.type

        # Nested class/interface/enum/record declarations: do NOT recurse;
        # their methods belong to their own class scoring.
        if t in CLASS_LIKE:
            return

        # Nested method declarations (rare but legal inside e.g. local class) --
        # handled when we see the class. Defensive: skip.
        if t in METHOD_LIKE:
            return

        if t == "lambda_expression":
            # Lambda body contributes to enclosing method, with nesting+1.
            lam_body = node.child_by_field_name("body")
            if lam_body is not None:
                # The body itself is the new nesting level.
                for c in lam_body.children if lam_body.is_named else []:
                    pass
                walk(lam_body, nesting + 1, inside_nested_decl)
            return

        # if-statement: detect else-if chain.
        if t == "if_statement":
            # +1 + nesting for the if itself.
            total += 1 + nesting
            cond = node.child_by_field_name("condition")
            cons = node.child_by_field_name("consequence")
            alt = node.child_by_field_name("alternative")
            if cond is not None:
                walk(cond, nesting, inside_nested_decl)
            if cons is not None:
                walk(cons, nesting + 1, inside_nested_decl)
            if alt is not None:
                # else-if: parse the inner if without bumping nesting further.
                if alt.type == "if_statement":
                    # fresh +1 for else-if, no extra nesting
                    total += 1
                    cond2 = alt.child_by_field_name("condition")
                    cons2 = alt.child_by_field_name("consequence")
                    alt2 = alt.child_by_field_name("alternative")
                    if cond2 is not None:
                        walk(cond2, nesting, inside_nested_decl)
                    if cons2 is not None:
                        walk(cons2, nesting + 1, inside_nested_decl)
                    if alt2 is not None:
                        # Recurse manually to keep the else-if chain detection.
                        _handle_else(alt2, nesting, inside_nested_decl)
                else:
                    # plain else: +1, body at nesting+1
                    total += 1
                    walk(alt, nesting + 1, inside_nested_decl)
            return

        if t == "ternary_expression":
            total += 1 + nesting
            # children: condition ? consequence : alternative (no fields in some grammars)
            cond = node.child_by_field_name("condition")
            cons = node.child_by_field_name("consequence")
            alt = node.child_by_field_name("alternative")
            if cond is not None:
                walk(cond, nesting, inside_nested_decl)
            if cons is not None:
                walk(cons, nesting + 1, inside_nested_decl)
            if alt is not None:
                walk(alt, nesting + 1, inside_nested_decl)
            return

        if t in ("switch_expression", "switch_statement"):
            total += 1 + nesting
            for c in node.children:
                walk(c, nesting + 1, inside_nested_decl)
            return

        if t in ("for_statement", "enhanced_for_statement", "while_statement", "do_statement"):
            total += 1 + nesting
            body_node = node.child_by_field_name("body")
            body_id = (body_node.start_byte, body_node.end_byte) if body_node else None
            for c in node.children:
                cid = (c.start_byte, c.end_byte)
                if body_id is not None and cid == body_id:
                    walk(c, nesting + 1, inside_nested_decl)
                else:
                    # Conditions / inits don't bump nesting but still need to
                    # be scanned for && || groups & recursion.
                    walk(c, nesting, inside_nested_decl)
            return

        if t == "catch_clause":
            total += 1 + nesting
            for c in node.children:
                walk(c, nesting + 1, inside_nested_decl)
            return

        # break / continue with label -> +1 each (no nesting)
        if t in ("break_statement", "continue_statement"):
            # Look for a child identifier (the label).
            for c in node.children:
                if c.type == "identifier":
                    total += 1
                    break
            return

        # Boolean operator groups (&&, ||): only count at top of chain.
        if t == "binary_expression":
            op_node = node.child_by_field_name("operator")
            op = node_text(op_node, SRC) if op_node is not None else ""
            if op in ("&&", "||") and is_top_level_boolean(node):
                total += boolean_groups(node, SRC)
                # children still walked for nested constructs (calls, ternaries inside)
            # fall through to recurse

        # Method invocation -> recursion detection.
        if t == "method_invocation":
            name_node = node.child_by_field_name("name")
            if name_node is not None and enclosing_method_name is not None:
                if node_text(name_node, SRC) == enclosing_method_name:
                    total += 1
            # fall through to recurse into args

        # default: recurse into all children
        for c in node.children:
            walk(c, nesting, inside_nested_decl)

    def _handle_else(alt_node, nesting, inside_nested_decl):
        """Handle the alternative of a nested else-if chain."""
        nonlocal total
        if alt_node.type == "if_statement":
            total += 1  # else-if
            cond2 = alt_node.child_by_field_name("condition")
            cons2 = alt_node.child_by_field_name("consequence")
            alt2 = alt_node.child_by_field_name("alternative")
            if cond2 is not None:
                walk(cond2, nesting, inside_nested_decl)
            if cons2 is not None:
                walk(cons2, nesting + 1, inside_nested_decl)
            if alt2 is not None:
                _handle_else(alt2, nesting, inside_nested_decl)
        else:
            total += 1
            walk(alt_node, nesting + 1, inside_nested_decl)

    walk(body, 0)
    return total


# ---------------------------------------------------------------------------
# Class / file traversal
# ---------------------------------------------------------------------------


def collect_classes(root, src: bytes):
    """Yield (fqn_stack_list_of_names, class_node) for every class-like node.

    fqn_stack_list_of_names = list of (kind, name) from outermost to innermost
    so outermost level is index 0.
    """
    # find package
    package = None
    for c in root.children:
        if c.type == "package_declaration":
            # name child contains dotted name
            for cc in c.children:
                if cc.type in ("scoped_identifier", "identifier"):
                    package = node_text(cc, src)
                    break

    results = []

    def recurse(node, stack):
        for c in node.children:
            if c.type in CLASS_LIKE:
                name = get_name(c, src) or "<anon>"
                new_stack = stack + [name]
                results.append((package, new_stack, c))
                # recurse into body to find nested classes/methods
                body = c.child_by_field_name("body")
                if body is not None:
                    recurse(body, new_stack)
            else:
                recurse(c, stack)

    recurse(root, [])
    return results


def fqn_of(package: Optional[str], stack: list[str]) -> str:
    inner = stack[0]
    for n in stack[1:]:
        inner += "$" + n
    return f"{package}.{inner}" if package else inner


def class_directly_declared_methods(class_node):
    """Yield method/constructor nodes declared *directly* in this class body.

    Skips methods inside nested classes/interfaces/enums/records.
    """
    body = class_node.child_by_field_name("body")
    if body is None:
        return
    for child in body.children:
        if child.type in METHOD_LIKE:
            yield child
        # Anonymous classes inside field initialisers etc. are handled when
        # their declaration node is visited at the top-level traversal (we
        # recurse into all bodies). They are *not* direct methods of `class_node`.


# ---------------------------------------------------------------------------
# File processing
# ---------------------------------------------------------------------------


def should_skip(path: Path) -> bool:
    s = str(path)
    if "/src/test/" in s or "/src/testFixtures/" in s:
        return True
    if "/build/" in s or "/.gradle/" in s or "/.pylibs/" in s:
        return True
    for _seg in EXTRA_PRUNE:
        if f"/{_seg}/" in s:
            return True
    return False


def process_file(abs_path: Path):
    """Return (per_class_rows, per_file_row, parse_error_bool).

    per_class_rows: list of (rel_file, class_fqn, class_complexity, method_count)
    per_file_row:   (rel_file, file_complexity, class_count, method_count)
    """
    global SRC
    try:
        SRC = abs_path.read_bytes()
    except OSError as e:
        print(f"warn: cannot read {abs_path}: {e}", file=sys.stderr)
        return [], (str(abs_path.relative_to(REPO)), 0, 0, 0), True

    tree = parser.parse(SRC)
    root = tree.root_node
    parse_error = root.has_error

    rel = str(abs_path.relative_to(REPO))

    classes = collect_classes(root, SRC)
    if not classes:
        return [], (rel, 0, 0, 0), parse_error

    per_class_rows = []
    file_complexity = 0
    file_method_count = 0

    for package, stack, cnode in classes:
        fqn = fqn_of(package, stack)
        c_complexity = 0
        m_count = 0
        for m in class_directly_declared_methods(cnode):
            mname = get_name(m, SRC)
            score = compute_method_complexity(m, mname)
            c_complexity += score
            m_count += 1
        per_class_rows.append((rel, fqn, c_complexity, m_count))
        file_complexity += c_complexity
        file_method_count += m_count

    per_file_row = (rel, file_complexity, len(classes), file_method_count)
    return per_class_rows, per_file_row, parse_error


def main():
    java_files: list[Path] = []
    for dirpath, dirnames, filenames in os.walk(REPO):
        # prune common build dirs early
        _prune = {".git", "build", ".gradle", ".pylibs", "node_modules", "out", ".idea"} | EXTRA_PRUNE
        dirnames[:] = [d for d in dirnames if d not in _prune]
        for fn in filenames:
            if fn.endswith(".java"):
                p = Path(dirpath) / fn
                if not should_skip(p):
                    java_files.append(p)

    java_files.sort()

    per_class_path = OUT_DIR / "complexity-per-class.tsv"
    per_file_path = OUT_DIR / "complexity-per-file.tsv"

    error_files = 0
    total_classes = 0
    all_class_rows: list[tuple[str, str, int, int]] = []
    all_file_rows: list[tuple[str, int, int, int]] = []

    for i, p in enumerate(java_files):
        rows, frow, err = process_file(p)
        if err:
            error_files += 1
            print(f"warn: parse errors in {p.relative_to(REPO)}", file=sys.stderr)
        all_class_rows.extend(rows)
        all_file_rows.append(frow)
        total_classes += len(rows)
        if (i + 1) % 500 == 0:
            print(f"... processed {i + 1}/{len(java_files)}", file=sys.stderr)

    with per_class_path.open("w", encoding="utf-8") as f:
        f.write("file\tclass_fqn\tclass_complexity\tmethod_count\n")
        for r in all_class_rows:
            f.write(f"{r[0]}\t{r[1]}\t{r[2]}\t{r[3]}\n")

    with per_file_path.open("w", encoding="utf-8") as f:
        f.write("file\tfile_complexity\tclass_count\tmethod_count\n")
        for r in all_file_rows:
            f.write(f"{r[0]}\t{r[1]}\t{r[2]}\t{r[3]}\n")

    # ---------- summary ----------
    print(f"\n=== Summary ===")
    print(f"Files processed: {len(java_files)}")
    print(f"Files with parse errors: {error_files}")
    print(f"Total classes: {total_classes}")
    print(f"Total methods/constructors: {sum(r[3] for r in all_file_rows)}")

    top_classes = sorted(all_class_rows, key=lambda r: r[2], reverse=True)[:10]
    print("\nTop 10 classes by complexity:")
    print("  complexity\tmethods\tfile\tclass_fqn")
    for r in top_classes:
        print(f"  {r[2]}\t{r[3]}\t{r[0]}\t{r[1]}")

    top_files = sorted(all_file_rows, key=lambda r: r[1], reverse=True)[:10]
    print("\nTop 10 files by complexity:")
    print("  complexity\tclasses\tmethods\tfile")
    for r in top_files:
        print(f"  {r[1]}\t{r[2]}\t{r[3]}\t{r[0]}")


if __name__ == "__main__":
    main()
