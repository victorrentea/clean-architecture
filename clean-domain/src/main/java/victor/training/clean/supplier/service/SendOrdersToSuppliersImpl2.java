package victor.training.clean.supplier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("client2")
@Service
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl2 implements OrderConfirmedEventHandler {
   private final SupplierService supplierService;

   @Override
   public void suppliersOrdersData(Long orderId) {

   }
}
