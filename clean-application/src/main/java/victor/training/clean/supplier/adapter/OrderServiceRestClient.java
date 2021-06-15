package victor.training.clean.supplier.adapter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.order.facade.dto.OrderDto;
import victor.training.clean.order.facade.dto.OrderLineDto;
import victor.training.clean.supplier.model.OrderLineVO;
import victor.training.clean.supplier.model.OrderVO;
import victor.training.clean.supplier.service.OrderServiceClient;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
public class OrderServiceRestClient implements OrderServiceClient {
   private final RestTemplate rest;
   @Value("${order.api.base-url}")

   private String baseUrl;
   @Override
   public OrderVO getOrder(long orderId) {
      RestTemplate restTemplate = new RestTemplate();

      OrderDto orderDto = restTemplate.getForObject(baseUrl + "{id}", OrderDto.class, orderId);
      // orderDto.toVo(); // if the mapping grows BIG and if you DON'T GENERATE THE DTOs From SWAGGER
      List<OrderLineVO> lines = orderDto.lines.stream().map(this::toOrderLineVO).collect(Collectors.toList());
      return new OrderVO(orderDto.clientId, lines);
   }

   private OrderLineVO toOrderLineVO(OrderLineDto dto) {
      return new OrderLineVO(dto.itemQuantity, dto.supplierId);
   }
}

