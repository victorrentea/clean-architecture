package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

/**
 * Port (Dependency Inversion): the domain states WHAT it needs - a {@link User} -
 * and lets the infrastructure decide HOW to get it (LDAP today, anything tomorrow).
 * The signature is shaped for the domain, not for the remote API.
 */
public interface UserProvider {
  User findByUsernamePart(String usernamePart);
}
