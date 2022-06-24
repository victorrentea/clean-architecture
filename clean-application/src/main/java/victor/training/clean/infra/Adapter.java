package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.insurance.entity.User;
import victor.training.clean.domain.insurance.service.IAdapter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Adapter implements IAdapter {
    @Autowired
    private LdapApi ldapApi;

    @Override
    public User retrieveUser(String username) {
        List<User> list = ldapApi
                .searchUsingGET(null, null, username.toUpperCase())
                .stream().map(this::convert)
                .collect(Collectors.toList());
        if (list.size() != 1) {
            throw new IllegalArgumentException("There is no single user matching username " + username);
        }
        return list.get(0);
    }

    private User convert(LdapUserDto ldapUser) {
        String fullName = ldapUser.getFname() + " " + ldapUser.getLname().toUpperCase();
        return new User(ldapUser.getUid(), fullName, ldapUser.getWorkEmail());
    }


}
