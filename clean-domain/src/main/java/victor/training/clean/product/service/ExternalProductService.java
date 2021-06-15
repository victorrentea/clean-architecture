package victor.training.clean.product.service;

import victor.training.clean.product.model.Product;

import java.util.Optional;

public interface ExternalProductService { // call outside

   Optional<Product> loadProduct(String productId);
}
