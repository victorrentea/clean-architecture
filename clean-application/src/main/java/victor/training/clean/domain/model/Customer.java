package victor.training.clean.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import victor.training.clean.facade.dto.CustomerDto;

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
	@ManyToOne
	private Site site;

//    public CustomerDto toDto() {
//		return CustomerDto.builder()
//				.id(getId())
//				.name(getName())
//				.email(getEmail())
//				.siteId(getSite().getId())
//				.creationDateStr(getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//				.build();
//    }
}
