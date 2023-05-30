package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import victor.training.clean.common.Adapter;
import victor.training.clean.insurance.domain.model.User;
import victor.training.clean.insurance.client.ExternalUserProvider;

import java.util.List;

// Adapter design pattern in GoF
//@Component
@RequiredArgsConstructor
@Adapter
class LdapUserClient implements ExternalUserProvider {
  private final LdapApi ldapApi;

//  public LdapUserDto method() {
//
//  }

  @Override
  public User fetchByUsername(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());


//    RestTempla/**/te restTemplate;
//    UserFromDto vo = restTemplate.getForObject("", args, UserFromDto.class);
//    return vo;
    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }
    LdapUserDto dto = dtoList.get(0);

    return fromDto(dto);
  }

  private static User fromDto(LdapUserDto dto) {
//    String username = dto.getUid();
//    if (username == null) {
//      username = "anonymous";
//    }

//    String username = Optional.ofNullable(dto.getUid()).orElse("anonymous");
    String username = dto.getUid() != null ? dto.getUid() : "anonymous";

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String workEmail = dto.getWorkEmail() != null ? dto.getWorkEmail().toLowerCase():null;
    return new User(username, fullName, workEmail);
  }

}
