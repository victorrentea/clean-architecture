package victor.training.clean.application.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import victor.training.clean.application.mapper.CustomerMapStruct;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
// Dto used to both QUERY and COMMAND use-cases ?
public record CustomerDto(
    Long id, // GET only (server-assigned)
//    @Min(18)
//    int age, // designing for the future can lead to over-engineering. YAGNI!

//    Country country,
    @NotBlank
    @Size(min = 5)
    String name,
//    @UniqueEmail custom jakarta validation annotaitons injected with a repo !!! DON'T DO IT.
    // don't hit networ from custom validation annotations
    @Email // 200 characets regex
    @NotNull
    String email,
    Long countryId,

    String shippingAddressCity, // GET only (updated via dedicated endpoint)
    String shippingAddressStreet, // GET only (updated via dedicated endpoint)
    String shippingAddressZip, // GET only (updated via dedicated endpoint)
//Customer customer, // soooooo bad!
    String createdDateStr, // GET only (server-assigned)

    Boolean gold, // GET & PUT
    String goldMemberRemovalReason, // GET & PUT(if gold changed true->false)
    int discountPercentage, // GET only (server-side computed)

    String legalEntityCode,
    Boolean discountedVat // GET only (server-side fetched)
) {

//  @Autowired  CustomerMapStruct mapper; WRONG
  public static CustomerDto.CustomerDtoBuilder fromEntity(Customer customer) {
    return builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDateStr(customer.getCreatedDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressStreet(customer.getShippingAddress().street())
        .shippingAddressCity(customer.getShippingAddress().city())
        .shippingAddressZip(customer.getShippingAddress().zip())

        .discountPercentage(customer.getDiscountPercentage())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        ;
  }

  public Customer toEntity() {
    Customer customer = new Customer(name());
    customer.setEmail(email());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(countryId())); // JPA knows to link to the COUNTRY table with that ID at insert time
//    customer.setCountry(countryRepo.findById(countryId())); // not necessary
    customer.setLegalEntityCode(legalEntityCode());
    return customer;
  }
}

//Does this mean we need to have at least one unit test for the CustomerDto class,
// since we introduced a mapping that can be prone to errors?

// NO. Don't UNIT test mapping logic alone, unless it's a very complex mapping. (imagine 10 x ifs)
// Stupid logic like this above, is tested with integration tests.
// 1 go through the entire stack, with a real database (@Testcontainer), real HTTP calls, real JSON serialization


// should Integration Tests sum up to coverage?
// only if the team takes it seriously. Coverage doesn't mean anything anyway... just conforts the biz