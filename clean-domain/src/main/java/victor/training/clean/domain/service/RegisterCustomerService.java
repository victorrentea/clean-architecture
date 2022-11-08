package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService { // action not noun (breaking a norm in OOP)
    private final CustomerRepo customerRepo;

    public void register(Customer customer) {
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
       customerRepo.save(customer);
        // Heavy business logic
    }

    // NO = it's garbate
//    public List<victor.training.clean.facade.dto.CustomerSearchResult> search(victor.training.clean.facade.dto.CustomerSearchCriteria searchCriteria) {
//        return customerRepo.search(searchCriteria);
//
//    }
}
