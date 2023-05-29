package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class LdapApiClient {
    private final LdapApi ldapApi;

    User findByUserName(String targetUsername) {
        List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

        if (dtoList.size() != 1) {
            throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
        }

        LdapUserDto dto = dtoList.get(0);

        return fromDto(dto);
    }

    User fromDto(LdapUserDto dto) {
        String userName = Optional.ofNullable(dto.getUid()).orElse("anonymous");
        String email = dto.getWorkEmail();
        String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
        return new User(userName, email, fullName);
    }
}