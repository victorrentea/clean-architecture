package victor.training.clean.common.events;

public class OrderConfirmedEvent {
//   long orderId, someId, stuff; // option A copy all Order fields into this cass
//   OrderDto orderDto; // B) a bit fuzzy in a single codebase: who own this structure? Order? Supplier?
//   Order order;// C) expose entity model to supplier
   private final long id; // D) the Supplier team will call back into order to get the data of that order.

   public OrderConfirmedEvent(long id) {
      this.id = id;
   }

   public long getId() {
      return id;
   }
}
