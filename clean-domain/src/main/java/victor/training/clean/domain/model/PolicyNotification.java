package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
// violation 4
public class PolicyNotification {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private InsurancePolicy policy;

  private LocalDateTime time = LocalDateTime.now();

  private String title;

  public PolicyNotification() {
  }

  public Long getId() {
    return this.id;
  }

  public InsurancePolicy getPolicy() {
    return this.policy;
  }

  public LocalDateTime getTime() {
    return this.time;
  }

  public String getTitle() {
    return this.title;
  }

  public PolicyNotification setId(Long id) {
    this.id = id;
    return this;
  }

  public PolicyNotification setPolicy(InsurancePolicy policy) {
    this.policy = policy;
    return this;
  }

  public PolicyNotification setTime(LocalDateTime time) {
    this.time = time;
    return this;
  }

  public PolicyNotification setTitle(String title) {
    this.title = title;
    return this;
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof PolicyNotification)) return false;
    final PolicyNotification other = (PolicyNotification) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final Object this$policy = this.getPolicy();
    final Object other$policy = other.getPolicy();
    if (this$policy == null ? other$policy != null : !this$policy.equals(other$policy)) return false;
    final Object this$time = this.getTime();
    final Object other$time = other.getTime();
    if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
    final Object this$title = this.getTitle();
    final Object other$title = other.getTitle();
    if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PolicyNotification;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $policy = this.getPolicy();
    result = result * PRIME + ($policy == null ? 43 : $policy.hashCode());
    final Object $time = this.getTime();
    result = result * PRIME + ($time == null ? 43 : $time.hashCode());
    final Object $title = this.getTitle();
    result = result * PRIME + ($title == null ? 43 : $title.hashCode());
    return result;
  }

  public String toString() {
    return "PolicyNotification(id=" + this.getId() + ", policy=" + this.getPolicy() + ", time=" + this.getTime() + ", title=" + this.getTitle() + ")";
  }

//  public void dontdothis(CustomerDto dto) { // violation 5
//  }
}
