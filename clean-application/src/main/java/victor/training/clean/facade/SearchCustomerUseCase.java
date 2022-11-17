package victor.training.clean.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SearchCustomerUseCase {
  private final EntityManager entityManager;

  @PostMapping("search")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria criteria) {
    String jpql = "SELECT new victor.training.clean.facade.dto.CustomerSearchResult(c.id, c.name)" +
                  " FROM Customer c " +
                  " WHERE 1=1 ";

    Map<String, Object> paramMap = new HashMap<>();

    if (criteria.getName() != null) {
      jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
      paramMap.put("name", criteria.getName());
    }

    // etc

    // or CriteriaBuilder, or QueryDSL

    TypedQuery<CustomerSearchResult> query = entityManager.createQuery(jpql, CustomerSearchResult.class);
    for (String paramName : paramMap.keySet()) {
      query.setParameter(paramName, paramMap.get(paramName));
    }
    return query.getResultList();
  }
}
