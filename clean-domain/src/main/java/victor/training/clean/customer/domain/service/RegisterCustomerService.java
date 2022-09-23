package victor.training.clean.customer.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.insurance.domain.door.QuotationDoor;
import victor.training.clean.insurance.domain.door.dto.QuotationRequestKnob;
import victor.training.clean.insurance.domain.service.QuotationService;

@RequiredArgsConstructor
@Service
public class RegisterCustomerService {
    private final CustomerRepo customerRepo;
    private final QuotationDoor quotationDoor;


    public void registerCustomer(Customer customer) {
       // Heavy business logic
       // Heavy business logic
       // Heavy business logic
       // TODO Where can I move this little logic? (... operating on the state of a single entity)
        int discountPercentage = customer.getDiscountPercentage();

        System.out.println("Biz Logic with discount " + discountPercentage);
       // Heavy business logic
       // Heavy business logic
       customerRepo.save(customer);

        if (customer.isGoldMember()) {
            QuotationRequestKnob knob = new QuotationRequestKnob(customer.getId(), customer.getName());
            quotationDoor.quoteCustomer(knob);
        }


       // Heavy business logic
       // Heavy business logic
       // Heavy business logic
       // Heavy business logic


    }

}
