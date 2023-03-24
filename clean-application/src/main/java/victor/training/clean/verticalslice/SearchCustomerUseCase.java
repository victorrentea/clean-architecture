package victor.training.clean.verticalslice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.join;

@SuppressWarnings("JpaQlInspection")
@RequiredArgsConstructor
@RestController
public class SearchCustomerUseCase {
  private final EntityManager entityManager;

  @Value
  // in/out structures kept private, inaccessible from other Use-Cases!
  private static class SearchCustomerRequest { // JSON
    String name;
    String email;
    Long siteId;
  }
  @Value
  private static class SearchCustomerResponse { // JSON
    long id;
    String name;
    // TODO add 'email' to results => only this file is impacted
  }

  @Operation(description = "Customer Search")
  @PostMapping("customer/search-vertical")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerRequest criteria) {
    String jpql = "SELECT new victor.training.clean.verticalslice.SearchCustomerUseCase$SearchCustomerResponse(c.id, c.name)" +
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

    if (criteria.siteId != null) {
      jpqlParts.add("c.site.id = :siteId");
      params.put("siteId", criteria.siteId);
    }

    String whereCriteria = join(" AND ", jpqlParts);
    var query = entityManager.createQuery(jpql + whereCriteria, SearchCustomerResponse.class);
    for (String paramName : params.keySet()) {
      query.setParameter(paramName, params.get(paramName));
    }
    return query.getResultList();
  }
}
