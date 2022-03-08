package victor.training.clean.entity;

import java.util.Objects;

// Value Object
public class User {
	private final String username;
	private final String fullName;
	private final String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = Objects.requireNonNull(username, "LDAP uid is null");
		this.fullName = fullName;
		this.workEmail = workEmail;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	public String getWorkEmail() {
		return workEmail;
	}
}
