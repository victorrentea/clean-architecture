package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final LdapApi ldapApi;

    public void importUserFromLdap(String targetUsername) {
        List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

        if (dtoList.size() != 1) {
            throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
        }

        LdapUserDto dto = dtoList.get(0);
        complexLogic(createUserFromDto(dto));
    }

    private void complexLogic(User user) {
        if (user.getWorkEmail().isPresent()) {
            checkNewUser(user.getWorkEmail().get());
        }

        log.debug("Insert user in my database: " + user.getUserName());
        log.debug("More logic for " + user.getFullName() + " of id " + user.getUserName());

        if (user.getWorkEmail().isPresent()) {
            sendMailTo(user.getToAddress());
            sendMailTo(user.getToAddress());
        }
    }

    private static User createUserFromDto(LdapUserDto dto) {
        String userName = dto.getUid() != null ? dto.getUid().toLowerCase() : "anonymous";
        String workEmail = dto.getWorkEmail() != null ? dto.getWorkEmail().toLowerCase() : null;
        String lastName = dto.getLname() != null ? dto.getLname().toUpperCase() : "";
        String fullName = dto.getFname() + " " + lastName;
        return User.builder()
                .userName(userName)
                .workEmail(workEmail)
                .fullName(fullName).build();
    }

    private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
        //... implementation left out
        log.debug("Contact: " + emailContact);
    }

    public void checkNewUser(String email) {
        log.debug("Check this user is not already in my system  " + email);
    }

}
