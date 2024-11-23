package victor.training.clean.domain.repo;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import victor.training.clean.domain.model.Customer;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmail(String email);


  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Customer> findById(Long aLong);

  boolean existsByLegalEntityCode(String legalEntityCode);
}
