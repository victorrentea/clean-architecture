package victor.training.clean.order.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.common.Adapter;
import victor.training.clean.order.adapter.dto.ExternalProductDto;
import victor.training.clean.product.model.Product;
import victor.training.clean.product.model.ProductPrice;
import victor.training.clean.product.service.ExternalProductService;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class ProductServiceRestClient implements ExternalProductService {
   private final RestTemplate rest;
   // you can make swagger gen plugin to generate a client also based on that yaml
   @Value("${product.api.base.url}")
   private String productUrlBaseUrl;

   @Value("${product.api.get-by-id.url}")
   private String getProductUrl;

   @Override
   public Optional<Product> loadProduct(String productId) {
//      rest.getForObject(productUrlBaseUrl + "/" + productId);
//      ExternalProductDto dto = rest.getForObject(getProductUrl, ExternalProductDto.class, productId);

      var response = rest.exchange(getProductUrl, HttpMethod.GET, RequestEntity.EMPTY, ExternalProductDto.class, productId);

      if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
         return Optional.empty();
      }

      return Optional.of(toEntity(response.getBody()));
   }

   private Product toEntity(ExternalProductDto dto) {
      ProductPrice price = new ProductPrice(dto.priceForRomania, dto.priceForItaly, dto.priceForSerbia);
      return new Product(dto.id, dto.name, price);
   }
}
