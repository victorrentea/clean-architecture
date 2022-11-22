package victor.training.clean.customer.domain.service;

import victor.training.clean.shared.domain.model.InsurancePolicy;

public class AnotherClas {
  // this should fail via the ArchUnit tes.
  public void badMethodCrossingTheBoundary(InsurancePolicy policy) {
    System.out.println("Stuff " + policy);
  }

}
