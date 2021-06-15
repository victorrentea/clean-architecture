package victor.training.clean.user.model;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class User {

   @EqualsAndHashCode
   @Embeddable
   public static class UserId implements Serializable {
      private String uuid = UUID.randomUUID().toString();

      public UserId() {} // generates Unique from java
      public UserId(String uuid) {
         this.uuid = uuid;
      }

      public String uuid() {
         return uuid;
      }
   }
   @EmbeddedId // generated in Java
   private UserId id = new UserId();

   private String username;

   protected User() {}
   public User(String username) {
      this.username = username;
   }

   public String getUsername() {
      return username;
   }
}
