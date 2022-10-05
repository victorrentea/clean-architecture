package victor.training.clean.domain.customer.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	@Size(min = 5)
	@Setter(AccessLevel.NONE)
	private String name;
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	private Site site;
	protected Customer() {}// just for hibernate
	public Customer(String name) {
		if (name.length() < 5) {
			throw new IllegalArgumentException();
		}
		this.name = name;}

	
	public int getDiscountPercentage() { // fixed the "Feature Envy" code smell
		//  PURE BUSINESS LOGIC (requirement) 2-5-7 lines , REUSABLE💪
		int discountPercentage = 3;
		if (this.goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}
