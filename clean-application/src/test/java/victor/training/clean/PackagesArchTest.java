package victor.training.clean;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Keeps {@code docs/packages.puml} in sync with the real package structure of {@code victor.training.clean}.
 *
 * <p>This is a living-documentation guardrail copied from the petclinic project. The PlantUML file is the
 * single source of truth for the <b>logical architecture</b>; these two tests fail the build the moment the
 * code drifts from the diagram, so the diagram can never silently rot.
 *
 * <p><b>When a test here fails, the fix is almost always to edit {@code docs/packages.puml}</b> so it matches
 * reality (add/remove a component, add/remove an arrow) — not to weaken the test.
 *
 * <ul>
 *   <li>{@link #adheresToDiagram} — every real cross-package dependency must be drawn as an arrow, and no
 *       arrow may exist that the code doesn't actually have.</li>
 *   <li>{@link #diagramPackagesMatchCodePackages} — the set of {@code <<..stereotype>>} components must equal
 *       the set of source sub-packages exactly, so a newly created (or deleted) package forces a diagram edit.</li>
 * </ul>
 */
@AnalyzeClasses(
    packages = "victor.training.clean",
    importOptions = DoNotIncludeTests.class
)
class PackagesArchTest {

  private static final Path DIAGRAM = Paths.get("docs/packages.puml");
  private static final Path SOURCE_ROOT = Paths.get("src/main/java/victor/training/clean");

  /**
   * Only dependencies whose target is part of the diagram are checked. This deliberately ignores
   * generated/bootstrap classes that openapi-generator emits into {@code victor.training.clean} and
   * {@code victor.training.clean.auth} (and the root {@code ApiClient}) — they are not part of the
   * hand-modelled logical architecture.
   */
  @ArchTest
  static final ArchRule adheresToDiagram =
      classes().should(adhereToPlantUmlDiagram(DIAGRAM, consideringOnlyDependenciesInDiagram()));

  @Test
  void diagramPackagesMatchCodePackages() throws IOException {
    Set<String> diagramPackages = parsePackagesFromDiagram();
    Set<String> codePackages = listCodePackages();

    Set<String> missingFromDiagram = new TreeSet<>(codePackages);
    missingFromDiagram.removeAll(diagramPackages);
    Set<String> staleInDiagram = new TreeSet<>(diagramPackages);
    staleInDiagram.removeAll(codePackages);

    assertThat(diagramPackages)
        .as("docs/packages.puml has drifted from the code packages of victor.training.clean.%n"
            + "  -> ADD a component for each NEW package:     %s%n"
            + "  -> REMOVE the component for each GONE package: %s%n"
            + "Edit clean-application/docs/packages.puml so every <<..stereotype>> maps to a real sub-package,%n"
            + "then draw/erase the arrows so PackagesArchTest.adheresToDiagram passes too.",
            missingFromDiagram, staleInDiagram)
        .isEqualTo(codePackages);
  }

  /** Captures the package suffix inside every {@code <<..suffix>>} stereotype in the diagram. */
  private static Set<String> parsePackagesFromDiagram() throws IOException {
    String puml = Files.readString(DIAGRAM);
    Pattern stereotype = Pattern.compile("<<\\.\\.([a-zA-Z0-9.]+)>>");
    Matcher matcher = stereotype.matcher(puml);
    Set<String> result = new TreeSet<>();
    while (matcher.find()) {
      result.add(matcher.group(1));
    }
    return result;
  }

  /** Every source sub-package of {@code victor.training.clean} that contains at least one .java file. */
  private static Set<String> listCodePackages() throws IOException {
    try (Stream<Path> paths = Files.walk(SOURCE_ROOT)) {
      return paths.filter(Files::isDirectory)
          .filter(dir -> !dir.equals(SOURCE_ROOT))
          .filter(PackagesArchTest::containsJavaFile)
          .map(dir -> SOURCE_ROOT.relativize(dir).toString().replace(java.io.File.separatorChar, '.'))
          .collect(Collectors.toCollection(TreeSet::new));
    }
  }

  private static boolean containsJavaFile(Path dir) {
    try (Stream<Path> entries = Files.list(dir)) {
      return entries.anyMatch(p -> p.toString().endsWith(".java"));
    } catch (IOException e) {
      return false;
    }
  }
}
