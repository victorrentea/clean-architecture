package victor.training.clean.domain.model.state.map;

import java.util.List;
import java.util.Map;

import static victor.training.clean.domain.model.state.map.CustomerStatus.*;
import static victor.training.clean.domain.model.state.map.Transition.to;

/**
 * Concrete wiring for the Customer aggregate.
 * {@code CustomerContext} is a minimal projection of the fields the guards need
 * &mdash; the state machine stays decoupled from the full {@code Customer} entity.
 * <p>
 * Usage:
 * <pre>{@code
 * CustomerContext ctx = new CustomerContext("alice");
 * INSTANCE.requireLegal(DRAFT, VALIDATED, ctx);   // ok
 * INSTANCE.requireLegal(DRAFT, ACTIVE, ctx);      // IllegalStateException
 * }</pre>
 */
public class CustomerStateMachineExample {

  public static final CustomerStateMachine<CustomerContext> INSTANCE =
      new CustomerStateMachine<>(Map.of(
          DRAFT, List.of(
              to(VALIDATED, "validatedBy != null", c -> c.validatedBy() != null),
              to(DELETED)),
          VALIDATED, List.of(
              to(ACTIVE),
              to(DELETED)),
          ACTIVE, List.of(
              to(DELETED))
      ));

  public record CustomerContext(String validatedBy) {
  }
}
