package victor.training.clean.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
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

}
