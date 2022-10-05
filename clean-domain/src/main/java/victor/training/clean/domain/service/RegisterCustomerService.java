package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.repo.CustomerRepo;

@Service
@Slf4j@RequiredArgsConstructor
public class RegisterCustomerService {
    private final CustomerRepo customerRepo;
    ;
    public void registerCustomer(Customer customer) {
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic

        // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
    }

}
