package victor.training.clean.facade.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import java.time.format.DateTimeFormatter;

@Builder
@Value
public class CustomerDto {
   Long id;
   String name;
   String email;
   Long siteId;
   String creationDateStr;

}
