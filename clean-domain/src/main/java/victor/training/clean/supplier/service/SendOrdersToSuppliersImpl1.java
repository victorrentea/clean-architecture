package victor.training.clean.supplier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.supplier.model.OrderLineVO;
import victor.training.clean.supplier.model.OrderVO;
import victor.training.clean.supplier.model.ProductWithQuantity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
//@Profile("client1")
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl1  implements OrderConfirmedEventHandler {
   private final SupplierService supplierService;
   private final OrderServiceClient orderServiceClient;

   @PostConstruct
   public void hello() {
      log.info("Using Supplier Impl 1");
   }

   @Override
   public void suppliersOrdersData(Long orderId) {

      OrderVO order = orderServiceClient.getOrder(orderId);

      Map<String, List<ProductWithQuantity>> result = order.orderLines().stream()
          .collect(groupingBy(OrderLineVO::supplierId,
              mapping(line -> new ProductWithQuantity(getProduct(line), line.itemQuantity()), toList())));
      supplierService.sendOrders(result);
   }
   private String getProduct(OrderLineVO line) {
      return "TODO";
   }
}
