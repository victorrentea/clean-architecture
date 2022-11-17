package victor.training.clean.customer.door.knob;

//tomorrow: DTO as JSON over REST
public final class CustomerKnob {
  private final long id;
  private final String name;

  public CustomerKnob(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
}
