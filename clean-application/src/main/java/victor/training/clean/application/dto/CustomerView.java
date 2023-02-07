package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Site;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerView { // Dto used to both QUERY and COMMAND use-cases ?
   Long id;
   String name;
   String email;
   Long siteId;
   String creationDateStr;
   boolean gold;
   String goldMemberRemovalComment;

   public CustomerView(Customer customer) {
     id=customer.getId();
     name=customer.getName();
     email=customer.getEmail();
     siteId=customer.getSite().getId();
     creationDateStr=customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
     gold=false;
     goldMemberRemovalComment =null;
   }
}
