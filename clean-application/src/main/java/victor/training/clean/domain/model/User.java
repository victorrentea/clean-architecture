package victor.training.clean.domain.model;

import java.util.Optional;

public record User(
    String username,
    // i **trust** my colleagues can do this TOMORRROW
//    String firstName,
    String fullName,
    Optional<String> email // the only acceptable place for Optional as field or param
    // #javasucks
) {
  public Optional<String> asContact() {
    return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
  }
}
