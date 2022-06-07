package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.entity.User;
import victor.training.clean.domain.service.ExternalUserService;

import java.util.List;

@RequiredArgsConstructor
@Adapter // the means to implement Anti-Corruption Layer
public class LdapUserServiceAdapter implements ExternalUserService {
    private final LdapApi ldapApi;

//    public LdapUserDto shitToTheDOmain() {
//        return  null;
//    }
    @Override
    public User getUserByUsername(String username) {
        List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());
        if (list.size() != 1) {
            throw new IllegalArgumentException("There is no single user matching username " + username);
        }
        LdapUserDto ldapUser = list.get(0);
        return map(ldapUser);
    }

    private User map(LdapUserDto ldapUser) {
        String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
        return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
    }

    public void bizLogicNice(User user) {

    }
    public void bizLogic(LdapUserDto hugeBloatedStructureFullOf80NotUsedFieldsToFreakOutAnyReader) {

    }
}
