package victor.training.clean.domain.model;

import lombok.NonNull;

import java.util.Optional;

public record User(
    @NonNull
    String username,
    @NonNull
    String fullName,
    @NonNull
    Optional<String> email // the only place in Java 17 allowed to have field/param of type Optional
) {

  public Optional<String> toEmailRecipient() {
//    return fullName + " <" + email.get() + ">";
    return email.map(e -> fullName + " <" + e + ">");
  }

//  public static void main(String[] args) {
//    new User("victor", "Victor Rentea", Optional.of(" [email protected]")).toEmailRecipient().ifPresent(System.out::println);
//    new User(null, "Victor Rentea", Optional.of(" [email protected]")).toEmailRecipient().ifPresent(System.out::println);
//  }
}
