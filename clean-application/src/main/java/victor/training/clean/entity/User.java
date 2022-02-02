package victor.training.clean.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
// for hibernate
public class User {
	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private String fullName;
	@Column(nullable = false)
	private String workEmail;

//	@ElementCollection
//	@ToString.Exclude
//	@JsonIgnore
//	private List<String> lista;
//
//	public List<String> getLista() {
//		return lista;
//	}

	public User(String username, String fullName, String workEmail) {
		this.username = username;
		this.fullName = fullName;
		this.workEmail = Objects.requireNonNull(workEmail);
	}

	private User() {
	}

	public Long getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getWorkEmail() {
		return this.workEmail;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public User setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}


	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) return false;
		final User other = (User) o;
		if (!other.canEqual(this)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		final Object this$username = this.getUsername();
		final Object other$username = other.getUsername();
		if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
		final Object this$fullName = this.getFullName();
		final Object other$fullName = other.getFullName();
		if (this$fullName == null ? other$fullName != null : !this$fullName.equals(other$fullName)) return false;
		final Object this$workEmail = this.getWorkEmail();
		final Object other$workEmail = other.getWorkEmail();
		return this$workEmail == null ? other$workEmail == null : this$workEmail.equals(other$workEmail);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof User;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final Object $username = this.getUsername();
		result = result * PRIME + ($username == null ? 43 : $username.hashCode());
		final Object $fullName = this.getFullName();
		result = result * PRIME + ($fullName == null ? 43 : $fullName.hashCode());
		final Object $workEmail = this.getWorkEmail();
		result = result * PRIME + ($workEmail == null ? 43 : $workEmail.hashCode());
		return result;
	}

	public String toString() {
		return "User(id=" + this.getId() + ", username=" + this.getUsername() + ", fullName=" + this.getFullName() + ", workEmail=" + this.getWorkEmail() + ")";
	}
}
