package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import victor.training.clean.domain.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmail(String email);

  // PASSes the ArchUnit test✅
//  @Query("SELECT new victor.training.clean.application.dto.CustomerDto(id,name) FROM Customer")
//  List<CustomerDto> search();

  // FAILSes the ArchUnit test❌
//  @Query("SELECT c FROM Customer c")
//  List<Customer> search();

  boolean existsByLegalEntityCode(String legalEntityCode);
}
