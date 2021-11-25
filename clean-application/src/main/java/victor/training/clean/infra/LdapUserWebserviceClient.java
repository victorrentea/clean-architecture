package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.infra.DummyData;
import victor.training.clean.infra.LdapUserDto;

import java.util.Arrays;
import java.util.List;

@Service
public class LdapUserWebserviceClient {

	public List<LdapUserDto> search(String uId, String fName, String lName) {
		// Imagine a search URL is formed here and a GET is then performed
		// Then, the response JSON list is converted to LdapUser objects
		return Arrays.asList(DummyData.ldapUser1/*, DummyData.ldapUser2*/);
	}
	
}
