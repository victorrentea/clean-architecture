package victor.training.clean.customer.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

// 1 avoid @Entity by using orm.xml (DON'T) - childlish: the way JPA locks you in is NOT the annotations.
// the way it couples you is the lifecycle of the attached Entities: auto-flushing at end of Tx, lazy load, @ManyToOne Site site; instead of Long siteId;

// 2 Create a separate @Entity model and map to/from your "DOMAIN entity" to Hibernate Entity: >>> causes anger in developers (too much mapping)
// typically chosen by "fanatic DDD Messiah"

// 3 tolerate @Entity but minimize the JPA invasion:
// no auto-flushing, less @Transactional, NO lazy load > LEFT JOIN FETCH, numeric FK instead of JPA links, use @Embeddable, @ElementCollection
// https://www.youtube.com/watch?v=iw0tOx7Zbjc&t=3827s

@Data
@Entity
public class Customer {
	@Setter(AccessLevel.NONE)
	@Id
	private Long id;
	@Length(min = 5)
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


	public void setSite(Site site) {
		this.site=site;
//		EventPubliser.publish(new CustomerSiteChangedEvent(site.id));
	}

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (isGoldMember()) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}
}