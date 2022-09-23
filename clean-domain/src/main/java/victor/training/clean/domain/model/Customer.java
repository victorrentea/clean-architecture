package victor.training.clean.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@ToString
@Getter
@Entity
// Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Size(min = 5)
	private String name;
	@Email // automat validate de Hibernate la .save()
	@Setter
	private String email;
	@Setter
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	@Setter
	private Site site;

	protected Customer() {} // doar pt hibernate
	public Customer(String name) { // pt devi
		if (name.length() < 5) {
			throw new IllegalArgumentException();
		}

		this.name = Objects.requireNonNull(name);
	}

    public int getDiscountPercentage() {
        int discountPercentage = 3;
		if (goldMember) {
           discountPercentage += 1;
        }
        return discountPercentage;
    }

}
