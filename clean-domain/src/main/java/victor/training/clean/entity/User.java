package victor.training.clean.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@NoArgsConstructor(access = PRIVATE) // for hibernate
public class User {
	@Id
	@GeneratedValue
	@Setter(NONE)
	private Long id;
	private String username;
	private String fullName;
	private String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = Objects.requireNonNull(username);
		this.fullName = fullName;
		this.workEmail = workEmail;
	}
// CRUD


	public boolean hasEmail() {
		return workEmail != null;
	}

	public String getWorkEmail() {
		return workEmail;
	}

	public String getFullName() {
		return fullName;
	}

	public String getUsername() {
		return username;
	}
}
