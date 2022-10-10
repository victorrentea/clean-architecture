package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.api.event.CustomerRegisteredEvent;
import victor.training.clean.customer.model.Customer;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.insurance.service.QuotationService;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService {
    private final CustomerRepo customerRepo;
    private final QuotationService quotationService;

    public void register(Customer customer) {
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is already registered");
        }

        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = 3;
        if (customer.isGoldMember()) {
            discountPercentage += 1;
        }
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        customerRepo.save(customer);
        // Heavy business logic

        eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId())); // exactly here in this moment, sync, all listeners are called.
        // in what order (2+) ?? PANIC: should'n matter
        // What if

//        quotationService.quoteCustomer(customer);
        // I do not need a result from the other side.

    }

    private final ApplicationEventPublisher eventPublisher;
}

