package victor.training.clean.domain.model;

import java.util.Optional;

// domain value object
public record User(
    String username,
    String fullName,
    Optional<String> email // the only place in Java where fields are allowed to be Optional<>

) {
  public boolean hasInternalEmail() {
//    return email.get().toLowerCase().endsWith("@cleanapp.com");
    return email.map(e -> e.toLowerCase().endsWith("@cleanapp.com")).orElse(false);
  }
//  @Override
//  public Optional<String> email() { // does not compile because you can't override a method with a different return type
//    return Optional.ofNullable(email);
//  }
}
