package victor.training.clean.entity;

import lombok.Value;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Value
public class User {
	String username;
	String fullName;
	String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = requireNonNull(username);
		this.fullName = fullName;
		this.workEmail = workEmail;
	}

	public Optional<String> getWorkEmail() {
		return Optional.ofNullable(workEmail);
	}
}
