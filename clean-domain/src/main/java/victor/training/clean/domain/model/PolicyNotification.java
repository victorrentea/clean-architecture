package victor.training.clean.domain.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@Data // violation 4
public class PolicyNotification {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private InsurancePolicy policy;

  private LocalDateTime time = LocalDateTime.now();

  private String title;

//  public void dontdothis(CustomerDto dto) { // violation 5
//  }
}
