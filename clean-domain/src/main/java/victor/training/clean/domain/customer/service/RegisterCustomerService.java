package victor.training.clean.domain.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.entity.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.insurance.service.QuotationService;

@RequiredArgsConstructor
@Service // HOLY DOMAIN. respect. peace. ying and yang; Buddha
public class RegisterCustomerService // 2000 lines of code, 2 years later;
{

    // Customer is 👑 (the largest @Entity here)
    private final CustomerRepo customerRepo;
    private final QuotationService quotationService;

    public void register(Customer customer) {


//        CustomerDto doesNotCompile;
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
        // Heavy business logic
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
        quotationService.quoteCustomer(customer);

//        kafka.send( stuffv ); // BLOW UP
//        messagesToSendRepo.save(new MessageToSend(Stuff));

        throw new RuntimeException("BUM 500");
    }

}
