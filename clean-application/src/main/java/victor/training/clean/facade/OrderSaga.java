package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderSaga {
//   private final StockManagementService stockManagementService;

   @Transactional
   public void placeOrder(String order) {
//   sendQueue(); nu mai faci


//      stockManagementService.reserveStock(order); // queue
//      paymentService.processPayment(order); // queue
//      invoiceService.generateInvoce(order); // queue
// catch (NoareBani) {stock.cancelStock(order);}
//      workflow.save();
//      mesagesToSend.insert(new message)
   }


   // intr-un pas de saga : UPDATE la tabela ta cu starea noua + SEND de mesaje pe coada

   // In loc sa trimiti mesajul tu pe coada, il INSEREZI in baza ta, si apoi folosesti un tool gen debezium
   // se preiau mesajele automat si se trimit pe cozi

   // daca vrei

}
