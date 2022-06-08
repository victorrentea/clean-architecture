package victor.training.clean.domain.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.domain.customer.api.event.CustomerRegisteredEvent;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.insurance.service.QuotationService;

@RequiredArgsConstructor
@Service
public class  RegisterCustomerService{
    private final CustomerRepo customerRepo;
    private final ApplicationEventPublisher eventPublisher;
//    private final QuotationService quotationService;


//    @Transactional
    public void register(Customer customer) {
        // EXTREME CLKEAN CODE. TESTS. TDD. PEACE.
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Where can I move this? (a bit of domain logic operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        customerRepo.save(customer);
        // Heavy business logic

        // we can remove this line by:
        // 1) earlier orchestration: the one calling register() should call quotation after.
        // 2) deep choreography: i fire an event from here.
//        quotationService.quoteCustomer(customer);
        eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));

//        jmsSender.send(new Message());
    }
}
