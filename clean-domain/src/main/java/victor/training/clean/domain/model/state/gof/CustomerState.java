package victor.training.clean.domain.model.state.gof;

/**
 * Gang-of-Four State pattern.
 * <p>
 * Each state is a type. Transitions are methods on the state &mdash; the legal
 * ones override the default, the illegal ones fall through to the default and
 * throw. No {@code switch (status)} anywhere; the compiler enforces exhaustive
 * handling via the {@code sealed} hierarchy.
 * <p>
 * Trade-off vs. the table-driven sketch: the *shape* of the graph is no longer
 * visible in one place &mdash; you have to read every state class to reconstruct
 * it. Worth it when each state needs to carry its own data (see
 * {@code ValidatedState.validatedBy}) or to override behavior beyond transitions.
 */
public sealed interface CustomerState
    permits DraftState, ValidatedState, ActiveState, DeletedState {

  default CustomerState validate(String validatedBy) {
    throw new IllegalStateException("validate() not allowed from " + getClass().getSimpleName());
  }

  default CustomerState activate() {
    throw new IllegalStateException("activate() not allowed from " + getClass().getSimpleName());
  }

  default CustomerState delete() {
    return new DeletedState();
  }
}
