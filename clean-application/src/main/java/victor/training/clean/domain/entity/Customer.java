package victor.training.clean.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

// Avoid on @Entity
@Entity
// business logic object
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private LocalDate creationDate = LocalDate.now();
    private boolean goldMember;
    @ManyToOne
    private Site site;
    protected Customer() {} // just for hibernate

    public Customer(String name) {
        if (name.length() < 5) {
            throw new IllegalArgumentException();
        }
        this.name = Objects.requireNonNull(name);
    }

//	public CustomerDto toDto()  { // end of the civilization as we know it.
		// violation of the oldest historically attested design pattern : M V C
//	}

    public int getDiscountPercentage() {
		// imagine a func working with stuff that does not belong to customer.
//		Then the func needs to get that other data as a parame.
		// maybe even

		// TODO Where can I move this little logic? (... operating on the state of a single entity)
		int discountPercentage = 3;
        if (goldMember) {
            discountPercentage += 1;
        }
        return discountPercentage;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public boolean isGoldMember() {
        return this.goldMember;
    }

    public Site getSite() {
        return this.site;
    }

    public Customer setId(Long id) {
        this.id = id;
        return this;
    }


    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }

    public Customer setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Customer setGoldMember(boolean goldMember) {
        this.goldMember = goldMember;
        return this;
    }

    public Customer setSite(Site site) {
        this.site = site;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Customer)) return false;
        final Customer other = (Customer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$creationDate = this.getCreationDate();
        final Object other$creationDate = other.getCreationDate();
        if (this$creationDate == null ? other$creationDate != null : !this$creationDate.equals(other$creationDate))
            return false;
        if (this.isGoldMember() != other.isGoldMember()) return false;
        final Object this$site = this.getSite();
        final Object other$site = other.getSite();
        if (this$site == null ? other$site != null : !this$site.equals(other$site)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Customer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $creationDate = this.getCreationDate();
        result = result * PRIME + ($creationDate == null ? 43 : $creationDate.hashCode());
        result = result * PRIME + (this.isGoldMember() ? 79 : 97);
        final Object $site = this.getSite();
        result = result * PRIME + ($site == null ? 43 : $site.hashCode());
        return result;
    }

    public String toString() {
        return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", creationDate=" + this.getCreationDate() + ", goldMember=" + this.isGoldMember() + ", site=" + this.getSite() + ")";
    }
}
