package victor.training.clean.supplier.adapter;

import lombok.extern.slf4j.Slf4j;
import victor.training.clean.common.Adapter;
import victor.training.clean.supplier.model.ProductWithQuantity;
import victor.training.clean.supplier.service.SupplierService;

import java.util.List;
import java.util.Map;

@Adapter
@Slf4j
public class SupplierServiceRestClient implements SupplierService {

   public String getSupplierIdForProduct(String productId) {
// TODO rest call
      String dummy = "supplier";
      return  dummy;
   }

   @Override
   public void sendOrders(Map<String, List<ProductWithQuantity>> result) {
      //new RestTemplate().post
      log.info("Sending orders to supplier " + result);
   }
}
