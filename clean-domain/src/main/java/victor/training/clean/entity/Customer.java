package victor.training.clean.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.time.LocalDate;

// consider encapsulating changes
@Entity
public class Customer {
	// KNOW this
	@Id
	@GeneratedValue
	private Long id;
	@Size(min = 5) // VALIDATION 2: automatically done by hibernate or your own ASPECT
	private String name;
	private String email;
	private LocalDate creationDate;
	private boolean goldMember;
	@ManyToOne
	private Site site;

	private Customer() {} // for hibernate

	public Customer(String name) {
		if (name.length() < 5) { // VALIDATION 3 : The best ~ DDD
			throw new IllegalArgumentException("Name too short");
		}
		this.name = name;
	}


	public boolean isGoldMember() {
		return goldMember;
	}

	public void setGoldMember(boolean goldMember) {
		this.goldMember = goldMember;
	}

	public int getDiscountPercentage() {
		int discountPercentage = 3;
		if (goldMember) {
			discountPercentage += 1;
		}
		return discountPercentage;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public Site getSite() {
		return site;
	}

	public Customer setEmail(String email) {
		this.email = email;
		return this;
	}

	public Customer setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public Customer setSite(Site site) {
		this.site = site;
		return this;
	}
}
