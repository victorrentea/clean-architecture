package victor.training.clean.customer.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Objects;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor(access = PRIVATE) // for hibernate
public class User {
	@Id
	@Setter(NONE)
	@EqualsAndHashCode.Exclude
	private Long id;
	private String username;
	private String fullName;
	private String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = username;
		this.fullName = Objects.requireNonNull(fullName);
		this.workEmail = workEmail;
	}
}
