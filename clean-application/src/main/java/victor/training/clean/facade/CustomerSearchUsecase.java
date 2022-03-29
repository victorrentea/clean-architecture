package victor.training.clean.facade;

import org.springframework.stereotype.Service;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;
import victor.training.clean.repo.CustomerSearchRepo;

import java.util.List;

@Service
public class CustomerSearchUsecase {
   private final CustomerSearchRepo customerSearchRepo;

   public CustomerSearchUsecase(CustomerSearchRepo customerSearchRepo) {
      this.customerSearchRepo = customerSearchRepo;
   }

   public List<CustomerSearchResult> search(CustomerSearchCriteria searchCriteria) {
      return customerSearchRepo.search(searchCriteria);
   }
}

//@Service
//public class PlaceOrder {
//   private final CustomerSearchRepo customerSearchRepo;
//
//   public CustomerSearchUsecase(CustomerSearchRepo customerSearchRepo) {
//      this.customerSearchRepo = customerSearchRepo;
//   }
//
//   public List<CustomerSearchResult> placeOrder(CustomerSearchCriteria searchCriteria) {
//      // 2000 LOC
//      return customerSearchRepo.search(searchCriteria);
//   }
//}
