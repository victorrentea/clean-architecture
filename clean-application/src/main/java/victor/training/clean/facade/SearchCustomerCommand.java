package victor.training.clean.facade;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.repo.CustomerSearchRepo;

import java.util.List;

@Server
@RequiredArgsConstructor
public class SearchCustomerCommand {

   private final CustomerSearchRepo customerSearchRepo;
   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }
}
