package victor.training.clean.application;

import victor.training.clean.domain.model.Customer;

public class CustomerHelper {
  public static int getDiscountPercentage(Customer customer) {
    int discountPercentage = 1;
    if (customer.isGoldMember()) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }
}
