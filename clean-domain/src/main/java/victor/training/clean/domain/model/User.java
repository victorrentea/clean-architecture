package victor.training.clean.domain.model;

import lombok.NonNull;

import java.util.Optional;

public record User(
  @NonNull
  String username,
  @NonNull
  String fullName,
  Optional<String> email
) {
  // logic here
}
