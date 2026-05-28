package victor.training.clean.domain.model.state.map;

import java.util.function.Predicate;

/**
 * One legal edge in the transition graph.
 * The {@code guard} can reference live aggregate state (e.g. "validatedBy != null"),
 * so arbitrary domain rules ride alongside the structural check.
 * The optional {@code label} is the human-readable form of the rule, used only
 * by the diagram generator &mdash; the runtime always reads {@code guard}.
 */
public record Transition<C>(CustomerStatus target, Predicate<C> guard, String label) {

  public static <C> Transition<C> to(CustomerStatus target) {
    return new Transition<>(target, ctx -> true, null);
  }

  public static <C> Transition<C> to(CustomerStatus target, String label, Predicate<C> guard) {
    return new Transition<>(target, guard, label);
  }
}
