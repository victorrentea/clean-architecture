package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.InsurancePolicy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
//  @Query("select p from InsurancePolicy p where p.customer.id = :customerId")
  InsurancePolicy findByCustomerId(long customerId);
}
