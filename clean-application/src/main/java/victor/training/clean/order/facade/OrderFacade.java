package victor.training.clean.order.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import victor.training.clean.common.Facade;
import victor.training.clean.order.facade.dto.OrderDto;
import victor.training.clean.order.facade.dto.OrderLineDto;
import victor.training.clean.order.model.Order;
import victor.training.clean.order.repo.OrderRepo;
import victor.training.clean.order.service.OrderService;

@Slf4j
@Facade
@RequiredArgsConstructor
public class OrderFacade {
   private final OrderRepo orderRepo;
   private final OrderService orderService;
   public void create(OrderDto dto) {
      Order order = new Order(dto.clientId);
      for (OrderLineDto lineDto : dto.lines) {
         order.add(lineDto.toEntity());
      }
      orderRepo.save(order);
   }

   public OrderDto getById(long id) {
      Order order = orderRepo.findById(id).get();
      return new OrderDto(order);
   }

   //   public List<Order> findPurpleOrders() { // special DOmain term for shipped but not paied orders
//      return orderRepo.findAll(shipped().and(not(payed())));
//   }
}
