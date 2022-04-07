//package victor.training.clean.repo.persistence;
//
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.Setter;
//import lombok.ToString;
//import victor.training.clean.domain.entity.Site;
//
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import java.time.LocalDate;
//
//@Data
//@ToString
//public class CustomerEntity {
//	@Setter(AccessLevel.NONE) // KNOW this
//	@Id
//	@GeneratedValue
//	private Long id;
//	private String name;
//	private String email;
//	private LocalDate creationDate;
//	private boolean goldMember;
//	@ManyToOne
//	private Site site;
//
//
//}
