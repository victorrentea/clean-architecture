package victor.training.clean.user.entity;

import org.apache.logging.log4j.util.Strings;

import java.util.Objects;
import java.util.Optional;

// Value Object
public class User {
	private final String username;
	private final String fullName;
	private final String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = Objects.requireNonNull(username);
		if (username.length() < 3) {
			throw new IllegalArgumentException("too short");
		}
		this.fullName = Objects.requireNonNull(fullName);
		this.workEmail = workEmail;
	}

	public boolean hasEmail() {
		return Strings.isNotEmpty(workEmail);
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
}
