package victor.training.clean.verticalslice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
//@RestController
public class SearchCustomerUseCase {
  private final EntityManager entityManager;

  @Value
  // private -> inaccessible from other Use-Cases!
  private static class SearchCustomerRequest { // <== JSON
    String name;
    String phone;
    Long siteId;
  }
  @Value
  private static class SearchCustomerResponse { // ==> JSON
    long id;
    String name;
  }


  @Operation(description = "Customer Search")
  @PostMapping("customer/search")
  public List<SearchCustomerResponse> search(@RequestBody SearchCustomerRequest criteria) {
    String jpql = "SELECT new victor.training.clean.application.SearchCustomerUseCase$SearchCustomerResponse(c.id, c.name)" +
                  " FROM Customer c " +
                  " WHERE 1=1 ";

    Map<String, Object> paramMap = new HashMap<>();

    if (criteria.getName() != null) {
      jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
      paramMap.put("name", criteria.getName());
    }
    // + more

    var query = entityManager.createQuery(jpql, SearchCustomerResponse.class);
    for (String paramName : paramMap.keySet()) {
      query.setParameter(paramName, paramMap.get(paramName));
    }
    return query.getResultList();
  }
}
