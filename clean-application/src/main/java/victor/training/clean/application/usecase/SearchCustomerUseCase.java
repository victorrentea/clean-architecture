package victor.training.clean.application.usecase;

import brave.http.HttpServerResponse;
import com.google.common.annotations.VisibleForTesting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.join;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchCustomerUseCase {
  private final EntityManager entityManager;
  @Value // @see lombok.config that allows Jackson to unmarshall via constructor into this class
  @Builder // used in tests
  @VisibleForTesting
  static class SearchCustomerCriteria {
    @Schema(description = "Part of the name, case-insensitive")
    String name;
    String email;
    Long countryId;
  }

  @Value
  static class SearchCustomerResult {
    long id;
    String name;
  }

//  @PostMapping("customer/search/export.xls")
//  public void search(@RequestBody SearchCustomerCriteria criteria, HttpServerResponse response) {
//    Stream<SearchCustomerResult> stream =  quer
//    searchExcelExporter.export(response, stream);
//  }
  @PostMapping("customer/search")
    public List<SearchCustomerResult> search(@RequestBody SearchCustomerCriteria criteria) {
    String jpql = "SELECT new victor.training.clean.application.usecase.SearchCustomerUseCase$SearchCustomerResult(c.id, c.name)" +
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

    if (criteria.getCountryId() != null) {
      jpqlParts.add("c.country.id = :countryId");
      params.put("countryId", criteria.getCountryId());
    }

    String whereCriteria = join(" AND ", jpqlParts);
    var query = entityManager.createQuery(jpql + whereCriteria, SearchCustomerResult.class);
    for (String paramName : params.keySet()) {
      query.setParameter(paramName, params.get(paramName));
    }
    return query.getResultList();
  }

}
