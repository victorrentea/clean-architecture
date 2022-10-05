package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.user.model.User;
import victor.training.clean.domain.user.service.ExternalUserProvider;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LdapAdapter implements ExternalUserProvider {
    private final LdapApi ldapApi;
    @Override
    public User retrieveByUsername(String username) {
        List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

        if (list.size() != 1) {
            throw new IllegalArgumentException("There is no single user matching username " + username);
        }

        LdapUserDto ldapUser = list.get(0);
        String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
        return new User(ldapUser.getUid(), ldapUser.getWorkEmail(), fullName);
    }
}
