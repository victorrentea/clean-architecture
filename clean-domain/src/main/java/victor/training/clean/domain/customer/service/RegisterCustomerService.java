package victor.training.clean.domain.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.api.events.CustomerRegisteredEvent;
import victor.training.clean.domain.customer.entity.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;

@RequiredArgsConstructor
@Service // HOLY DOMAIN. respect. peace. ying and yang; Buddha
public class RegisterCustomerService // 2000 lines of code, 2 years later;
{

    // Customer is 👑 (the largest @Entity here)
    private final CustomerRepo customerRepo;

    public void register(Customer customer) {


//        CustomerDto doesNotCompile;
        // Heavy business logic
        // Heavy business logic
        // Where can I move this? (a bit of domain logic operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();
        System.out.println("Biz Logic with discount " + discountPercentage);
        // Heavy business logic
        // Heavy business logic
        customerRepo.save(customer); // COMMITED
//        customerRepo.save(stuff2);
        // Heavy business logic
        // option1 : InsuranceApi call --> REST
        // option2: ochjestrate the call form "EARLIER" from the Facade\ --> SAGA
        // option3: Events (Observer Pattern) --> QUEUES
//        quotationService.quoteCustomer(customer);
        eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId()));
//        kafka.send( stuffv ); // BLOW UP
//        messagesToSendRepo.save(new MessageToSend(Stuff));

        throw new RuntimeException("BUM 500");
    }
    private final ApplicationEventPublisher eventPublisher;

}
