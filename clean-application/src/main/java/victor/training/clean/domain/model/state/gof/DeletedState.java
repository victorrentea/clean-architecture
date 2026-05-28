package victor.training.clean.domain.model.state.gof;

public record DeletedState() implements CustomerState {

  @Override
  public CustomerState delete() {
    throw new IllegalStateException("Already deleted");
  }
}
