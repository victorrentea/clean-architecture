package victor.training.clean.supplier.service;


import victor.training.clean.supplier.model.ProductWithQuantity;

import java.util.List;
import java.util.Map;

public interface SupplierService {
   void sendOrders(Map<String, List<ProductWithQuantity>> result);
}
