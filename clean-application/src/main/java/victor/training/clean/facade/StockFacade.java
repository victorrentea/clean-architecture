package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Facade;
import victor.training.clean.service.NotificationService;

@Facade
@RequiredArgsConstructor
public class StockFacade {
private final NotificationService notificationService;
	public void checkStock() {
		// 3 lines of domain Logic

		// TODO same as in CustomerFacade but with other subject/body
		notificationService.orderWasInStock("a@b.com");
	}
}
