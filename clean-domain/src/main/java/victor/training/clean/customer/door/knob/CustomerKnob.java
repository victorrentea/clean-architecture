package victor.training.clean.customer.door.knob;

// at some point it might become a Dto over REST...
public class CustomerKnob {
  private final long id;
  private final String name;

  public CustomerKnob(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }
}
