package victor.training.clean.supplier.service;

import victor.training.clean.supplier.model.OrderVO;

public interface OrderServiceClient {
   OrderVO getOrder(long orderId);
}
