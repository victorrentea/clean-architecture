package victor.training.clean.domain.model.state.gof;

/**
 * Demonstrates the sealed-class GoF state machine end-to-end.
 *
 * <p>The aggregate holds a {@code CustomerState} reference. Mutation happens
 * by *replacing* that reference with the result of a transition method.
 * Each state object is immutable; per-state data (like {@code validatedBy})
 * is carried by the corresponding record.
 *
 * <p>Because {@code CustomerState} is {@code sealed}, a {@code switch} over
 * it is exhaustive at compile time:
 * <pre>{@code
 * String label = switch (holder.state()) {
 *   case DraftState d        -> "new";
 *   case ValidatedState v    -> "validated by " + v.validatedBy();
 *   case ActiveState a       -> "active";
 *   case DeletedState ignored -> "gone";
 * };
 * }</pre>
 */
public class CustomerStateGofExample {

  public static void main(String[] args) {
    Holder holder = new Holder();
    System.out.println(holder.state());           // DraftState[]
    holder.validate("alice");
    System.out.println(holder.state());           // ValidatedState[validatedBy=alice]
    holder.activate();
    System.out.println(holder.state());           // ActiveState[validatedBy=alice]
    holder.delete();
    System.out.println(holder.state());           // DeletedState[]

    try {
      holder.delete();                            // already deleted -> blows up
    } catch (IllegalStateException ex) {
      System.out.println("Refused second delete: " + ex.getMessage());
    }
  }

  /**
   * Tiny aggregate-of-one wrapping a state reference.
   */
  public static class Holder {
    private CustomerState state = new DraftState();

    public CustomerState state() {
      return state;
    }

    public void validate(String validatedBy) {
      state = state.validate(validatedBy);
    }

    public void activate() {
      state = state.activate();
    }

    public void delete() {
      state = state.delete();
    }
  }
}
