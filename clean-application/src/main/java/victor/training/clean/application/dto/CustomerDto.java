package victor.training.clean.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id; // GET (from sequence in DB)

  String name; // *

  String email; // *

  Long countryId; // *

  String creationDateStr; // GET (server-side assigned)

  boolean gold; // GET & PUT
  String goldMemberRemovalReason; // GET & PUT if gold=true->false
  int discountPercentage; // GET (server-side computed)

  String legalEntityCode; // *
  boolean discountedVat; // GET (server-side fetched)

//  public CustomerDto(Customer customer) {
//    id = customer.getId();
//    name = customer.getName();
//    email = customer.getEmail();
//    countryId = customer.getCountry().getId();
//    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
//    gold = customer.isGoldMember();
//    goldMemberRemovalReason = customer.getGoldMemberRemovalReason();
//    legalEntityCode = customer.getLegalEntityCode();
//    discountedVat = customer.isDiscountedVat();
//    discountPercentage = ?
//  }
}
