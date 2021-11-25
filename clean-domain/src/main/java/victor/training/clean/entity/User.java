package victor.training.clean.entity;

//@DDD.ValueObject
public class User { // value object
	private final String username;
	private final String fullName;
	private final String workEmail;

	public User(String username, String fullName, String workEmail) {
		this.username = username;
		this.fullName = fullName;
		this.workEmail = workEmail;
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
