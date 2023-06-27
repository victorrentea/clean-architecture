package victor.training.clean.domain.model.insurance;

import lombok.Data;
import victor.training.clean.domain.model.insurance.InsurancePolicy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class PolicyNotification {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private InsurancePolicy policy;

  private LocalDateTime time = LocalDateTime.now();

  private String title;
}
