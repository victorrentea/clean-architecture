package victor.training.clean.domain.entity;

import java.util.Objects;
import java.util.Optional;

// Value Object
public class User {
	private final String username;
	private final String fullName;
	private final String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = Objects.requireNonNull(username);
		this.fullName = Objects.requireNonNull(fullName);
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

	public boolean hasEmail() {
		return workEmail != null;
	}
}
