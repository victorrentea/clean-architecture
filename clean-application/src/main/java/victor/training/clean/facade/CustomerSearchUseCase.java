//package victor.training.clean.facade;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import victor.training.clean.facade.dto.CustomerSearchCriteria;
//import victor.training.clean.facade.dto.CustomerSearchResult;
//import victor.training.clean.repo.CustomerSearchRepo;
//
//import java.util.List;
//@RequiredArgsConstructor
//@Service
//public class CustomerSearchUseCase {
//   private final CustomerSearchRepo customerSearchRepo;
//   public List<CustomerSearchResult> execute(CustomerSearchCriteria searchCriteria) {
//      return customerSearchRepo.search(searchCriteria);
//   }
//}
//// overengineering
////@Servic
////public cleass PlaceOrderUseCase4K {
////   private final CustomerSearchRepo customerSearchRepo;
////   public List<CustomerSearchResult> execute(CustomerSearchCriteria searchCriteria) {
////      return customerSearchRepo.search(searchCriteria);
////   }
////
////}
