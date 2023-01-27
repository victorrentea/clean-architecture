package victor.training.clean.domain.model;

import javax.persistence.Embeddable;

@Embeddable
// value object o grupare mica imutabila de date fara PK
public class FullName {
  private String firstName;
  private String lastName;

protected FullName() {} //pt sufletuy lu hibernate
  public FullName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
