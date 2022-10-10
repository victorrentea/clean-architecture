package victor.training.clean.facade.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// sent as JSON
public class CustomerSearchResult {
   private final long id;
   private final String name;

   public CustomerSearchResult(long id, String name) {
      this.id = id;
      this.name = name;
   }

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}
