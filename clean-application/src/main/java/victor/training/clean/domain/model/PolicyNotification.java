package victor.training.clean.domain.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
