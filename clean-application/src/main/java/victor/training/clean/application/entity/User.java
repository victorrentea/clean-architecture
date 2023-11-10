package victor.training.clean.application.entity;


import java.util.Optional;

public record User(
    String username,
// Opt1: daca ai nevoie de first sau last singur
//    String firstName,
//    String lastName,
    // Opt2: e mai simplu si pt teste
    String fullName,

    Optional<String> email) {
  public boolean isCleanApp() {
    return email.map(String::toLowerCase)
        .map(s->s.endsWith("@cleanapp.com"))
        .orElse(false);
  }

  // Opt1:
//  public void fullName() {
//    return firstName + " " + lastName.toUpperCase()
//  }
}
