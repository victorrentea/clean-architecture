package victor.training.clean.domain.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

// Value Object al MEU!
public class User {
	private final String username;
	private final String fullName;
	private final String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = requireNonNull(username, "Missing username");
		this.fullName = requireNonNull(fullName);
		this.workEmail = workEmail;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	public Optional<String> getWorkEmail() {
		return Optional.ofNullable(workEmail);
	}

	public boolean hasWorkEmail() {
		return workEmail != null;
	}
}
