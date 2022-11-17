package victor.training.clean.domain.service;

import victor.training.clean.infra.LdapUserPhoneDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

// This is a Value Object = small, immutable, hash/eq on all fields, with no persistent identity
public class UserFromDto {
  // - i don't generate this Dto from an Open API but I hand-craft it.
  // TODO invoke a validator on this object once you get it back from the response

  // private fields mapped to <=> JSON
  @NotNull
  private String uid;

  private String fname;

  private String lname;

  private String workEmail;
  // private String creationDate; // not mapped from JSON (others 4 skipped to)

  // Using my domain names (ubiquitous language)
  public String getUsername() {
    return uid;
  }

  // Transforming data on-the-fly
  public String getFullName() {
    return fname + " " + lname.toUpperCase();
  }

  // - Null-safe
  public Optional<String> getEmail() {
    return ofNullable(workEmail);
  }

  // More Logic inside

  // No setters!
}
