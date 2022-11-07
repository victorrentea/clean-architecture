package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.service.IUserProviderAdapter;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class UserProviderAdapter implements IUserProviderAdapter {
    private final LdapApi ldapApi;

    @Override
    public User fetchUser(String username) {
        List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

        if (list.size() != 1) {
            throw new IllegalArgumentException("There is no single user matching username " + username);
        }

        LdapUserDto ldapUser = list.get(0);
        String fullName = ldapUser.getFname() + " " +
                          ldapUser.getLname().toUpperCase();
        User user = new User(ldapUser.getUid(), Optional.of(ldapUser.getWorkEmail()), fullName);
        return user;
    }
}
