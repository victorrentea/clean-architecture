package victor.training.clean.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Embeddable
@EqualsAndHashCode
class CustomerName {// a value object wrapping around just the name

//	@NotNull
//	@NotBlank//(groups = ActivatedCustomerState.class)
//	@Size(min = 3)
	@ValidCustomerName
	private String name;

	public CustomerName(String name) {
		if (StringUtils.isBlank(name) || name.length() < 3) {
			throw new IllegalArgumentException("Customer name is not valid");
		}
		this.name = name;
//		validate(this); // javax validator magically injected
	}

	public String getName() {
		return name;
	}
}

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	@NotNull
	private CustomerName name;

//	private String firstName, lastName;


	//	private String name;
	@NotNull
	@Email
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
	@ManyToOne
	private Site site;

}
