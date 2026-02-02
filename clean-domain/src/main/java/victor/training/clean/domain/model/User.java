package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email) {
}
//
//@Value // = @Getter + @AllArgsConstructor +
/// / @EqualsAndHashCode + @ToString
/// / private final on all fields
//public class User {
//  String username;
//  String fullName;
//  String email;
//
//  public Optional<String> getEmail() {
//    return Optional.ofNullable(email);
//  }
//}
