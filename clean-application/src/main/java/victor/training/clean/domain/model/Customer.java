package victor.training.clean.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@ToString
@Setter// consider encapsulating changes
@Getter
@Entity
// Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	private Site site;

  public int getDiscountPercentage() {
      int discountPercentage = 3;
      if (isGoldMember()) {
          discountPercentage += 1;
      }
      return discountPercentage;
  }
}
