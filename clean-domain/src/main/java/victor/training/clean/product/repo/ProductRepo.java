package victor.training.clean.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.product.model.Product;

public interface ProductRepo extends JpaRepository<Product, String> {

}
