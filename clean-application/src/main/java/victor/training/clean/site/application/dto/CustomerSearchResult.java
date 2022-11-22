package victor.training.clean.site.application.dto;

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
