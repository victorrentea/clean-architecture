package victor.training.clean.domain.model;

import java.util.Optional;

public record User(String username, String name, Optional<String> email) {
  public static final String MY_DOMAIN = "cleanapp.com";

  public boolean isInternalEmail() { // ubiquituous language. biz can understand this 😇
    return email.map(e -> e.toLowerCase().endsWith("@" + MY_DOMAIN)).orElse(false);
  }
}
