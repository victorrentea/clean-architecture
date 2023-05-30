package victor.training.clean.application.usecase;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import static java.time.format.DateTimeFormatter.ofPattern;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GetCustomerByIdUseCase {
  // you put each REST API
  private final CustomerRepo customerRepo;
//  private final SearchRepo repo
//  private final CustomerMapStruct mapStruct;
  // PRO: strictly required dependencies -> less @Mock in tests !:)
  // PRO: you need less Domain Services, more UC-specific  complexity can be kept outside the domain
  // CONS: if you have many stupid endpoints -> too many small classes
  // RISK⭐️: how can I REUSE stuff between the slices? ==> extract [Domain🙏] Services shared by usecase
  // RISK: UseCase classes grow > 150 lines => extract [Domain🙏] Services, mappers, validators/ push login in @ENtity
  // ? is this suitable for apps with complex domains (weird consistency rules, the kind of logic for which you'd turn to Aggregates/DDD)
  // CONS: more friction to adjust the API: you need to break clasess when splitting an API
  // ? how to test this : @SpringBootTest the whole thing. IF the complexity is large
  //   -> you need finer-grained unit tests for the detailed domain logic that you already probably moved in Domain Service
  @Builder
  @Value
  @VisibleForTesting // = slices should not call eachother
  static class GetCustomerByIdResponse {
    Long id;
    String name;
    String email;
    Long countryId;
    String creationDateStr;
    boolean gold;
    String goldMemberRemovalReason;
    int discountPercentage;
    String legalEntityCode;
    boolean discountedVat;
  }
//  interface SearchRepo extends Repository<Customer, Long> {
//    Optional<Customer> findById(Long id);
//  }
//  @Mapper(componentModel = "spring")
//  interface CustomerMapStruct {
//    @Mapping(target = "creationDateStr", source = "creationDate", dateFormat = "yyyy-MM-dd")
//    @Mapping(target = "countryId", source = "country.id")
//    GetCustomerByIdResponse toDto(Customer customer);
//  }
  @GetMapping("customer/{id}")
  public GetCustomerByIdResponse findById(@PathVariable long id) {
//    mapStruct
    Customer customer = customerRepo.findById(id).orElseThrow();
    return new GetCustomerByIdResponse.GetCustomerByIdResponseBuilder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .creationDateStr(customer.getCreationDate().format(ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())
        .discountPercentage(customer.getDiscountPercentage())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .build();
  }
}
