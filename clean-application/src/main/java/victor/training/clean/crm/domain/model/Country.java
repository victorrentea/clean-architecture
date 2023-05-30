package victor.training.clean.crm.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Country {
  @Id
  @GeneratedValue
  private long id;
  private String name;
  private String iso;

  public Country() {
  }

  public Country(String name, String iso) {
    this.name = name;
    this.iso = iso;
  }
}
