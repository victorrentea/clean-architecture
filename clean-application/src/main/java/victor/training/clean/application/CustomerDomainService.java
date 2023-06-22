package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.SearchCustomerCriteria;
import victor.training.clean.application.dto.SearchCustomerResponse;
import victor.training.clean.application.repo.CustomerSearchRepo;
import victor.training.clean.common.DomainService;

import java.util.List;

//@RequiredArgsConstructor
//@DomainService
//public class CustomerDomainService {
//  private final CustomerSearchRepo customerSearchRepo;
//
//  public List<SearchCustomerResponse> search(SearchCustomerCriteria searchCriteria) {
//    return customerSearchRepo.search(searchCriteria);
//  }
//}
