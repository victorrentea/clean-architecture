package victor.training.clean.customer.door.knob;

// ± internal Dto moving between modules
public class CustomerKnob {
  private final long id;
  private final String name;

  public CustomerKnob(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
