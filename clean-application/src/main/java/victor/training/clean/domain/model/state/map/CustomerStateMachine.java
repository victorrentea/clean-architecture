package victor.training.clean.domain.model.state.map;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Table-driven state machine. The entire graph is one immutable map,
 * which makes the rules trivial to audit, diff, or render as a diagram.
 *
 * Trade-off: per-state data or behavior has nowhere to live here &mdash;
 * if states need their own fields or polymorphic methods, prefer the
 * GoF variant in the sibling {@code gof} package.
 */
public class CustomerStateMachine<C> {

  private final Map<CustomerStatus, List<Transition<C>>> graph;

  public CustomerStateMachine(Map<CustomerStatus, List<Transition<C>>> graph) {
    this.graph = new EnumMap<>(graph);
  }

  /** Read-only view of the underlying graph &mdash; used by the diagram generator. */
  public Map<CustomerStatus, List<Transition<C>>> graph() {
    return Map.copyOf(graph);
  }

  /** Throws if {@code from -> to} is undeclared or its guard rejects {@code context}. */
  public void requireLegal(CustomerStatus from, CustomerStatus to, C context) {
    Transition<C> match = graph.getOrDefault(from, List.of()).stream()
        .filter(t -> t.target() == to)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "No transition declared: " + from + " -> " + to));

    if (!match.guard().test(context)) {
      throw new IllegalStateException(
          "Guard failed for transition: " + from + " -> " + to);
    }
  }
}
