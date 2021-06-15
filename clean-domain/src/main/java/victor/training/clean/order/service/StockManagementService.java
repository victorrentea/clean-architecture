package victor.training.clean.order.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class StockManagementService {
   private CacheManager cacheManager;

   public void getStock(Long productId) {
      Cache cache = cacheManager.getCache("stock");
      Long oldStock= (Long) cache.get(productId).get();
      // TODO query some Rel DB
   }
   public void decreaseStock(Long productId, int amount) {
      // TOOD update in a rel DB
      Cache cache = cacheManager.getCache("stock");
      Long oldStock= (Long) cache.get(productId).get();
      cache.put(productId, oldStock - amount);
   }


//
//
//   @Cacheable("stock")
//   public void getStock(Long productId) {
//      // TODO query some Rel DB
//   }
//   @CacheEvict(cacheNames = "stock", key = "#productId")
//   public void decreaseStock(Long productId, int amount) {
//      // TOOD update in a rel DB
//   }
//
//

}
