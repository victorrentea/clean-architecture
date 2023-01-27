package victor.training.clean.verticalslice;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SearchCustomerUseCase {
//  private final OInterfataDeSearch interfata;
  private final EntityManager entityManager;

  @Value
  // private!
  private static class Request { // <== JSON da private ca nu cumva alt cod din proj sa le vada
    String name;
    String phone;
    Long siteId;
  }
  @Value
  // private!
  private static class Response { // ==> JSON
    long id;
    String name;
  }


  @Operation(description = "Customer Search")
  @PostMapping("customer/search2")
  public List<Response> search(@RequestBody Request criteria) {
    String jpql = "SELECT new victor.training.clean.application.SearchCustomer$Response(c.id, c.name)" +
                  " FROM Customer c " +
                  " WHERE 1=1 ";

    Map<String, Object> paramMap = new HashMap<>();

    if (criteria.getName() != null) {
      jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
      paramMap.put("name", criteria.getName());
    }
    // + more

    var query = entityManager.createQuery(jpql, Response.class);
    for (String paramName : paramMap.keySet()) {
      query.setParameter(paramName, paramMap.get(paramName));
    }
    return query.getResultList();
  }
}
