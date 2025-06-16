package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
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

  public long getId() {
    return this.id;
  }

  public Country setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public Country setName(String name) {
    this.name = name;
    return this;
  }

  public String getIso() {
    return this.iso;
  }

  public Country setIso(String iso) {
    this.iso = iso;
    return this;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof Country)) return false;
    final Country other = (Country) o;
    if (!other.canEqual((Object) this)) return false;
    if (this.getId() != other.getId()) return false;
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final Object this$iso = this.getIso();
    final Object other$iso = other.getIso();
    if (this$iso == null ? other$iso != null : !this$iso.equals(other$iso)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof Country;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long $id = this.getId();
    result = result * PRIME + (int) ($id >>> 32 ^ $id);
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $iso = this.getIso();
    result = result * PRIME + ($iso == null ? 43 : $iso.hashCode());
    return result;
  }

  public String toString() {
    return "Country(id=" + this.getId() + ", name=" + this.getName() + ", iso=" + this.getIso() + ")";
  }
}
