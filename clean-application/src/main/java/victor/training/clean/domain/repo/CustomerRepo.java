package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import victor.training.clean.domain.model.Customer;

import java.util.List;
import java.util.Optional;

// Victor: when I look at the Repo interface I want to see here all the methods my app really uses.
public interface CustomerRepo extends Repository<Customer, Long>, JpaSpecificationExecutor<Customer> {

  Optional<Customer> findById(Long id);

  Customer save(Customer customer);

  List<Customer> findAll();

  boolean existsByEmail(String email);

  boolean existsByLegalEntityCode(String legalEntityCode);
}
