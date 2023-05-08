package victor.training.clean.verticalslice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.*;

import static java.lang.String.join;

@RequiredArgsConstructor
@RestController
public class SearchCustomerUseCase {
  private final EntityManager entityManager;

  // received as JSON from a search screen in Frontend
  @Value // @see lombok.config that allows Jackson to unmarshall via constructor into this class
  @Builder // for tests
  public static class CustomerSearchCriteria {
    String name;
    String email;
    Long siteId;
  }

  // sent as JSON to a search results grid in UI
  @Value
  public static class CustomerSearchResult {
    long id;
    String name;
  }


  @Operation(description = "Customer Search")
  @PostMapping("customer/search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria criteria) {

    // 50 linii de biz logic

    String jpql = "SELECT new victor.training.clean.verticalslice.SearchCustomerUseCase$CustomerSearchResult(c.id, c.name)" +
                  " FROM Customer c " +
                  " WHERE ";
    List<String> jpqlParts = new ArrayList<>();
    jpqlParts.add("1=1"); // alternatives: Criteria API ± Spring Specifications or Query DSL
    Map<String, Object> params = new HashMap<>();

    if (criteria.getName() != null) {
      jpqlParts.add("UPPER(c.name) LIKE UPPER('%' || :name || '%')");
      params.put("name", criteria.getName());
    }

    if (criteria.getEmail() != null) {
      jpqlParts.add("UPPER(c.email) = UPPER(:email)");
      params.put("email", criteria.getEmail());
    }

    if (criteria.getSiteId() != null) {
      jpqlParts.add("c.site.id = :siteId");
      params.put("siteId", criteria.getSiteId());
    }

    String whereCriteria = join(" AND ", jpqlParts);
    var query = entityManager.createQuery(jpql + whereCriteria, CustomerSearchResult.class);
    for (String paramName : params.keySet()) {
      query.setParameter(paramName, params.get(paramName));
    }
    return query.getResultList();
  }


}
