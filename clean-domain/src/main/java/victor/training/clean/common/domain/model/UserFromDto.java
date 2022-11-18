package victor.training.clean.common.domain.model;

import java.util.Optional;

import static java.util.Optional.ofNullable;

// this is a hand-crafted Dto
// - its private fields map to the external JSON
// - speaks my domain with its public methods
// This is a Value Object = small, immutable, hash/eq on all fields, with no persistent identity
public class UserFromDto {

  // private fields mapped to <=> JSON
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
