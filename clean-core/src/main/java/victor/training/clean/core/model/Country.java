package victor.training.clean.core.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
