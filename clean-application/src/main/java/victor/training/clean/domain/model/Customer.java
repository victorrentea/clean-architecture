package victor.training.clean.domain.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import victor.training.clean.application.dto.CustomerDto;

import javax.persistence.*;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

// un Value Object (VO) este un obiect
// - immutabil
// - mic (de obiecei)
// - nu are PK indentitate persistenta (lacks continuity of change)
// - hash/equals pe toate campurile
@Embeddable
class FullName {
	private String firstName;
	private String lastName;

	protected FullName() {} // doar pt hibernate
	public FullName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}
}

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
//	@Embedded
//	private FullName name;

	// la orice repo.save se valideaza aceste adnotari
				// sau custom @Aspect sa validezi orice param de tipul Customer
//	@Size(min = 5)
//	@NotNull
	private String name;

	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;


//	@Transient
//	String gunoi
//	@OneToMany
//	List<Copil> copii;

//	protected Customer() {} // for hibernate

//	public Customer(String name) {
//		if (name.length() < 5) { // DDD-like
//			// mai prost ca adnotarile ca arunca doar prima exceptie
//			// mai complicat in teste (ca ob trebuie sa fie valide)
//			throw new IllegalArgumentException("Name too short");
//		}
//		this.name = name;
//	}

	// null-safe model: getteri pe campuri care pot fi null sa de Optional
//	public Optional<String> getEmail() {
//		return Optional.ofNullable(email);
//	}

	// reguli de domeniu in model!
	// cand pui logica in model.. faci OOP
	// 		Mai ales daca sunt folosite in mai multe locuri (DRY Principle = Don't Repeat Yourself)
	// - Cand sa NU pui logica aici (in Domain Model @Entity < > DTOuri=APImodel)
	//   * logica ce implica datele altor entitati
	//   * interactiuni cu sisteme externe (retea): Repo/ApiCalls
	//   * presentation (formatari, parsari), chestii super specifice unui sg usecase
	//   * nici prea multa logica ( < 7-10 ) asa incat sa nu fii nevoit sa mockuiesti met asta.

//	@JsonIgnore // gu_noi
	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}

	// NU CUMVA!!!! GU-NOI: poluezi modelul cu un detaliu de API
//	public CustomerDto toDto() {
//		return CustomerDto.builder()
//						.id(getId())
//						.name(getName())
//						.email(getEmail())
//						.siteId(getSite().getId())
//						.creationDateStr(getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//						.build();
//	}

	//	public String asCSV() { // = presentation; depinde de altu; MVC
//		return name + ";"+ email.toUpperCase()+ ";"+ creationDate.format(LDT)
//	}
}
