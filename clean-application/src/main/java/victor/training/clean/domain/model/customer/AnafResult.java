package victor.training.clean.domain.model.customer;

public class AnafResult {
  private final String name;
  private final boolean isVatPayer;

  public AnafResult(String name, boolean isVatPayer) {
    this.name = name;
    this.isVatPayer = isVatPayer;
  }

  public String getName() {
    return name;
  }

  public boolean isVatPayer() {
    return isVatPayer;
  }
}
