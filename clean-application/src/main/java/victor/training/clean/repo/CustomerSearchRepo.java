package victor.training.clean.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import victor.training.clean.facade.dto.CustomerSearchCriteria;
import victor.training.clean.facade.dto.CustomerSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerSearchRepo {
   private final EntityManager entityManager;

   public List<CustomerSearchResult> search(CustomerSearchCriteria criteria) {
      String jpql = "SELECT new victor.training.clean.facade.dto.CustomerSearchResult(c.id, c.name)" +
                    " FROM Customer c " +
                    " WHERE 1=1 ";

      Map<String, Object> paramMap = new HashMap<>();

      if (StringUtils.isNotEmpty(criteria.getName())) {
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
