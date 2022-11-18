package victor.training.clean.application;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.repo.CustomerSearchRepo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@UseCase
public class CustomerSearchUseCase {
  private final EntityManager entityManager;
  // + all that has to do with a use case is in ONE FILE
    // "grouping code by axis of change"
  // + easier to test, as all the deps are  needed

  // - what if the use case is criminal ? 1000 lines in total.
  // + following SRP: ONE BEHAVIOR = 1 CLASS

  @Operation(description = "Customer Search")
  @PostMapping("customer/search-feature-slice")
  public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria criteria) {
    // selecting Dtos from JPA
    String jpql = "SELECT new victor.training.clean.application.dto.CustomerSearchResult(c.id, c.name)" +
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
