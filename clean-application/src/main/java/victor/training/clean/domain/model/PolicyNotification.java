package victor.training.clean.domain.model;

import lombok.Data;

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
