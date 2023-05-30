package victor.training.clean.crm.domain.model;

public class LegalEntity {
  private final String name;
  private final boolean isVatPayer;

  public LegalEntity(String name, boolean isVatPayer) {
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
