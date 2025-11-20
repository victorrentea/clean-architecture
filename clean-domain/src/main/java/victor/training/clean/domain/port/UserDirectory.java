package victor.training.clean.domain.port;

import victor.training.clean.domain.model.User;

// dependency inversion = soliD
// > useful in non-trivial apps
// WHY?
// a) ⭐️ you can test the domain easier (tech-agnostic)
// b) less mental load - you can reason about your own logic in isolation
// c) in theory you could reimplement this interf on top of IAM (AWS)/KeyCloak instead of LDAP
public interface UserDirectory {
  User findSingleByUsernamePart(String usernamePart);
}
