package victor.training.clean.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	// reguli de domeniu in model!
	// cand pui logica in model.. faci OOP
	// 		Mai ales daca sunt folosite in mai multe locuri (DRY Principle = Don't Repeat Yourself)
	// - Cand sa NU pui logica aici (in Domain Model @Entity < > DTOuri=APImodel)
	//   * logica ce implica datele altor entitati
	//   * interactiuni cu sisteme externe (retea): Repo/ApiCalls
	//   * presentation (formatari, parsari), chestii super specifice unui sg usecase
	//   * nici prea multa logica ( < 7-10 ) asa incat sa nu fii nevoit sa mockuiesti met asta.

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}

//	public String asCSV() { // = presentation; depinde de altu; MVC
//		return name + ";"+ email.toUpperCase()+ ";"+ creationDate.format(LDT)
//	}
}
