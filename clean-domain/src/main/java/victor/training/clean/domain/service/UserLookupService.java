package victor.training.clean.domain.service;

public interface UserLookupService {
  User lookupUser(String usernamePart);

  record User(String username, String fullName, String workEmail) {
  }
}
