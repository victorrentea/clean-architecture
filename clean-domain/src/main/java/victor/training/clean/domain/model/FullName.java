package victor.training.clean.domain.model;

import javax.persistence.Embeddable;

// un Value Object (VO) este un obiect
// - immutabil
// - mic (de obiecei)
// - nu are PK indentitate persistenta (lacks continuity of change)
// - hash/equals pe toate campurile
@Embeddable
public class FullName {
  private String firstName;
  private String lastName;

  protected FullName() {
  } // doar pt hibernate

  public FullName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }
}
