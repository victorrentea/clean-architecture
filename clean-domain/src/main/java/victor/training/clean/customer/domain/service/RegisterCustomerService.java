package victor.training.clean.customer.domain.service;

import org.springframework.stereotype.Service;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.door.QuotationServiceForCustomer;
import victor.training.clean.insurance.door.InsuranceDoor;

@Service

// promoted into the Domain, into a better world, only using MY DOMAIN MODEL.
public class RegisterCustomerService {// action, not noun. it's a piece of logic.
  private final CustomerRepo customerRepo;
  private final QuotationServiceForCustomer quotationServiceForCustomer;

  public RegisterCustomerService(CustomerRepo customerRepo, QuotationServiceForCustomer quotationServiceForCustomer, QuotationServiceForCustomer quotationService1, InsuranceDoor insuranceDoor) {
    this.customerRepo = customerRepo;
    this.quotationServiceForCustomer = quotationService1;
    this.insuranceDoor = insuranceDoor;
  }


  public Customer register(Customer customer) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic


    // TODO Where can I move this little logic?
    //    (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
//    customerRepo.save(customer);
//    if (customer.getSite() == null) {
//    }
    // Heavy business logic

    // option A (dep inversion😱 a bit more scary): customer module does not KNOW
    // (is not Coupled) to the insurance module: insurance -> customer
    // I ❤️ to protect to customer <== I don't couple it to insurance
//    quotationServiceForCustomer.quoteCustomer(customer.getId());
//      // similary to event-driven
//
//    // option B (just calls) customer -> insurance
//    insuranceDoor.quoteCustomer(customer.getId());

    return customer;
  }
  private final InsuranceDoor insuranceDoor;

}

