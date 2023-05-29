package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.LdapClientInterface;
import victor.training.clean.domain.model.User;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service

public class LdapClient implements LdapClientInterface {

	private final LdapApi ldapApi;

	@Override
	public User getUserByName(String name) {
		List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, name.toUpperCase());

		if (dtoList.size() != 1) {
			throw new IllegalArgumentException("Expected single user to match username " + name + ", got: " + dtoList);
		}

		LdapUserDto dto = dtoList.get(0);

		User user = new User();
		user.setUsername(dto.getUid());
		if (user.getUsername() == null)
			user.setUsername("anonymous");
		String fullname = dto.getFname();
		if (dto.getLname() != null)
			fullname += " " + dto.getLname().toUpperCase();
		user.setFullName(fullname);
		if (dto.getWorkEmail() != null)
			user.setEmail(dto.getWorkEmail().toLowerCase());
		return user;
	}
}
