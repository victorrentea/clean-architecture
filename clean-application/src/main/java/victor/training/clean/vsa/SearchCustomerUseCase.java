package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;

@SuppressWarnings("JpaQlInspection")
@RequiredArgsConstructor
@RestController
// spargi mai tarziu class PlaceOrderUseCase dacat OrderService.placeOrder()
public class SearchCustomerUseCase {
  private final EntityManager entityManager;

  @VisibleForTesting
  record SearchCustomerRequest( // JSON
      String name,
      String email,
      Long countryId
  ) {  }

  record SearchCustomerResponse( // JSON
      long id,
      String name,
      // TODO if we add 'email' to results => only this file is impacted
      String email
  ) { }

  @Operation(description = "Customer Search")
  @PostMapping("customer/search")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerRequest criteria) {
    String jpql = "SELECT new victor.training.clean.vsa.SearchCustomerUseCase$SearchCustomerResponse(" +
                  " c.id, c.name, c.email)" +
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

    if (criteria.countryId != null) {
      jpqlParts.add("c.country.id = :countryId");
      params.put("countryId", criteria.countryId);
    }

    String whereCriteria = join(" AND ", jpqlParts);
    var query = entityManager.createQuery(jpql + whereCriteria, SearchCustomerResponse.class);
    for (String paramName : params.keySet()) {
      query.setParameter(paramName, params.get(paramName));
    }
    return query.getResultList();
  }
}
