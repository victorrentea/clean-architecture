package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public record Username(String value) {
//  public Username(String p) { // infra garbage specific to LDAP
//      this.value = "".equals(p)?null:p;
//  }
}
