package victor.training.clean.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

// An anemic Domain Entity (fullly opened with getters and setters, no encapsulation)
@Entity

// *** LOMBOK BEST PRACTICES ***
// @Data - avoid. Instead:
@Getter @Setter
@ToString // @Exclude the child collections fields to avoid accidental lazy loading (Hibernate)
// @NoArgsConstructor(access = AccessLevel.PRIVATE) // PRO: keep the default constructor only for the persistence (Hibernate/nosql)
// @EqualsAndHashCode - usually a bad practice on Hibernate @Entity!
public class Customer {
	@Setter(AccessLevel.NONE) // KNOW this
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	public boolean isGoldMember() {
		return goldMember;
	}

	public void setGoldMember(boolean goldMember) {
		this.goldMember = goldMember;
	}
}
