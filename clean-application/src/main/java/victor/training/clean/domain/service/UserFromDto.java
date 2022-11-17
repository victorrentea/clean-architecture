package victor.training.clean.domain.service;

import victor.training.clean.infra.LdapUserPhoneDto;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

////Value Object = small, immutable and hash/eq on all fields. no persistent identity.
//public class UserFromDto {
//
//  private String creationDate; // not needed from JSON
//
//  private String departmentId;
//
//  private List<LdapUserPhoneDto> emailAddresses = null;
//
//  private String fname;
//
//  private String language;
//
//  private String lname;
//
//  private String uid;
//
//  private String workEmail;
//  private final String username;
//  private final String email;
//  private final String fullName;
//
//  public UserFromDto(String username, String email, String fullName) {
//    this.username = requireNonNull(username);
//    this.email = email;
//    this.fullName = fullName;
//  }
//
//  public String getFullName() {
//    return fullName;
//  }
//
//  public String getUsername() {
//    return username;
//  }
//
//  public Optional<String> getEmail() {
//    return ofNullable(email);
//  }
//}
