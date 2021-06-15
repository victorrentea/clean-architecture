package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import victor.training.clean.order.model.Order;
import victor.training.clean.order.model.OrderLine;
import victor.training.clean.order.model.SupplierId;
import victor.training.clean.order.repo.OrderRepo;
import victor.training.clean.order.service.OrderService;

import java.math.BigDecimal;

//@Profile("insertDummyData") // in real project
@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

   private final OrderRepo orderRepo;
   private final OrderService orderService;

   @Override
   public void run(String... args) throws Exception {


      Order order = new Order("clientId");
      order.add(new OrderLine("Pfeiser", BigDecimal.TEN, BigDecimal.ONE).supplierId(new SupplierId("eMAG")));
      orderRepo.save(order);
      System.out.println(orderRepo.findAll());

      orderService.suppliersOrdersData(order);



   }

}
