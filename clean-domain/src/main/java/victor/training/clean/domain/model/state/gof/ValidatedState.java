package victor.training.clean.domain.model.state.gof;

public record ValidatedState(String validatedBy) implements CustomerState {

  @Override
  public CustomerState activate() {
    return new ActiveState(validatedBy);
  }
}
