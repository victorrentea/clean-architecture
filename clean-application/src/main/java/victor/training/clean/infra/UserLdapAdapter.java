package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import victor.training.clean.domain.client.UserAdapter;
import victor.training.clean.domain.model.User;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserLdapAdapter implements UserAdapter {

    private final LdapApi ldapApi;

    public User getUserByUsername(String targetUsername) {
        List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

        if (dtoList.size() != 1) {
            throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
        }

        LdapUserDto dto = dtoList.get(0);
        return new User(dto.getUid(), dto.getWorkEmail(), dto.getFname(), dto.getLname());
    }

}
