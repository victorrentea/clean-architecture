package victor.training.clean.domain.model.state.gof;

import java.util.Objects;

public record DraftState() implements CustomerState {

  @Override
  public CustomerState validate(String validatedBy) {
    return new ValidatedState(Objects.requireNonNull(validatedBy));
  }
}
