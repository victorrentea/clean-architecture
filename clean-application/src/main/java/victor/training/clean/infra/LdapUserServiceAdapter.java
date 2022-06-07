package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.entity.User;
import victor.training.clean.domain.service.ExternalUserService;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class LdapUserServiceAdapter implements ExternalUserService {
    private final LdapApi ldapApi;

    public LdapUserDto shitToTheDOmain() {
        return  null;
    }
    @Override
    public User getUserByUsername(String username) {
        List<LdapUserDto> list = searchByUsername(username);
        if (list.size() != 1) {
            throw new IllegalArgumentException("There is no single user matching username " + username);
        }
        LdapUserDto ldapUser = list.get(0);
        return map(ldapUser);
    }

    private User map(LdapUserDto ldapUser) {
        String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
        User user = new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
        return user;
    }

    private List<LdapUserDto> searchByUsername(String username) {
        return ldapApi.searchUsingGET(null, null, username.toUpperCase());
    }


}
