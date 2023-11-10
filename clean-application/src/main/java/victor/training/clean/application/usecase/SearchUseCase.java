//package victor.training.clean.application.usecase;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RestController;
//import victor.training.clean.application.entity.Country;
//import victor.training.clean.application.entity.Customer;
//import victor.training.clean.application.repo.CustomerRepo;
//import victor.training.clean.in.rest.dto.CustomerDto;
//import victor.training.clean.in.rest.dto.SearchCustomerCriteria;
//import victor.training.clean.in.rest.dto.SearchCustomerResponse;
//
//import java.util.List;
//
//import static java.util.Objects.requireNonNull;
//
//@Slf4j
//@RequiredArgsConstructor
//public class SearchUseCase {
//
//  private final CustomerRepo customerRepo;
//  private final NotificationService notificationService;
//  private final CustomerSearchRepo customerSearchRepo;
//  private final InsuranceService insuranceService;
//
//
//  public List<SearchCustomerResponse> search(SearchCustomerCriteria searchCriteria) {
//    return customerSearchRepo.search(searchCriteria);
//  }
//
//}
