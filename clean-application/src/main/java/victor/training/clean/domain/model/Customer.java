package victor.training.clean.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data // TODO remove:
// Avoid lombok @Entity on ORM Domain @Entity
// 1) hashCode on @Id [ORM]
// 2) toString lazy-loading collections [ORM]
// 3) setters for everything = lack of encapsulation
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;

	// 🤔 Hmm... 3 fields with the same prefix TODO ?
	private String shippingAddressCity;
	private String shippingAddressStreet;
	private Integer shippingAddressZipCode;

	private LocalDate creationDate;
	private boolean goldMember;
	private String goldMemberRemovalReason;

	@ManyToOne
	private Site site;
}
