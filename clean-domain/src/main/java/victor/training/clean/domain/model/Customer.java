package victor.training.clean.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
//	@NotNull
//	@Pattern(regexp = "[^Aa].*")
	private String name;
//	private String firstName;
//	private String lastName;
//	@Embedded
//	private FullName fullName;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

//	@JsonIgnore // doamne fereste: modelu tau acum are +1 raspundere: sa se preznte frumos ca json
//	@OneToMany
//	private List<Phone> phoneList;

//	public Optional<String> getEmail() {
//		return Optional.ofNullable(email);
//	}

	protected Customer() {}// pt hibernate ❤️

	public Customer(String name) {
		this.name = Objects.requireNonNull(name);
	}
//	@AssertTrue
//	public boolean isValidRegulaComplexa() {
//		return false;
//	}

	//	public boolean isActive() { // ::isActive
//	}
	public int getDiscountPercentage() { // OOP tata! logica pusa in model
		// nu pui orice rahat in model. ci doar bucati mici (<5-7 linii) de domain knowledge (ideal reusable).
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}

// cand face victor scandal: cand vede 
class CustomerHelper { // aka Util aka ghena
	public static void method() {
		
	}
} 