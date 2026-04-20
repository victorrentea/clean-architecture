package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    String fullName,
    Optional<String> email

    //UserEmail email
//OrderId{string} orderId
) {
}


// micro-type
//record UserEmail(
//    String value
//){
//  UserEmail {
//    if (!value.contains("@")) {
//      throw new IllegalArgumentException();
//    }
//  }
//}