package victor.training.clean.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

import static java.util.Optional.ofNullable;

// Value Object la care voi mapa datele din Dto extern
@Value //=love e mai bun ca @Data = hate
// imutable
public class User {
  @NonNull // >> pune in constructorul invizibil un if care arunca NPE daca primeste null
  String username;
  @NonNull
  String fullName;
  String email;

  public Optional<String> getEmail() {
    return ofNullable(email);
  }

  //EmailUtil {}
  public String asEmailContact() {
      return getFullName() + " <" + getEmail() + ">";
    }
}
