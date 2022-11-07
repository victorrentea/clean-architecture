package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Data // Avoid on @Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	private LocalDate creationDate = LocalDate.now();
	private boolean goldMember;
//	List<Childern>
	@ManyToOne
	private Site site;

}
