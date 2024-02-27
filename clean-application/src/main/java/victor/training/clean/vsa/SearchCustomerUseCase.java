package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;

@RequiredArgsConstructor
@RestController
public class SearchCustomerUseCase {
  private final EntityManager entityManager; // ORM

  @VisibleForTesting // only @Tests are allowed to use this
  record SearchCustomerRequest(
      String name,
      String email
  ) {
  }

  @VisibleForTesting
  record SearchCustomerResponse(
      long id,
      String name,
      String email
      // TODO also return 'email' => only this file is impacted
  ) {
  }
//    LdapApi   ldapApi;

  @Operation(description = "Customer Search Poem")
  @PostMapping("customer/search-vsa")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerRequest criteria) {
//    List<LdapUserDto> criteria1 = ldapApi.searchUsingGET("criteria", null, null);


    String jpql = "SELECT new victor.training.clean.vsa.SearchCustomerUseCase$SearchCustomerResponse(c.id, c.name, c.email)" +
                  " FROM Customer c " +
                  " WHERE ";
    List<String> jpqlParts = new ArrayList<>();
    jpqlParts.add("1=1"); // alternatives: Criteria API ± Spring Specifications or Query DSL
    Map<String, Object> params = new HashMap<>();

    if (criteria.name != null) {
      jpqlParts.add("UPPER(c.name) LIKE UPPER('%' || :name || '%')");
      params.put("name", criteria.name);
    }

    if (criteria.email != null) {
      jpqlParts.add("UPPER(c.email) = UPPER(:email)");
      params.put("email", criteria.email);
    }
//
//    if (criteria.countryId != null) {
//      jpqlParts.add("c.country.id = :country");
//      params.put("countryId", criteria.countryId);
//    }

    String whereCriteria = join(" AND ", jpqlParts);
    var query = entityManager.createQuery(jpql + whereCriteria, SearchCustomerResponse.class);
    for (String paramName : params.keySet()) {
      query.setParameter(paramName, params.get(paramName));
    }
    return query.getResultList();
  }
}
