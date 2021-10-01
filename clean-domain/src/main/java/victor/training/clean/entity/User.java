package victor.training.clean.entity;

import lombok.Data;

@Data
public class User {
	private final  String username;
	private final  String fullName;
	private final  String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = username;
		this.fullName = fullName;
		this.workEmail = workEmail;
	}
}
