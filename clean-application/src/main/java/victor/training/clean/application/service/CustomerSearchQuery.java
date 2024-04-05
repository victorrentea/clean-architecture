package victor.training.clean.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;

// "Use-Case Optimized Query" pattern: SELECTing directly into DTOs sent out as JSONs.
// ⚠️ DON'T change data in DB!
// ⚠️ DON'T write heavy logic on the SELECTed DTOs!
// For both points above, load and use the full Domain Model @Entity.
@Repository
@RequiredArgsConstructor
public class CustomerSearchQuery {
   private final EntityManager entityManager;
private final JdbcTemplate jdbcTemplate;
   public List<CustomerSearchResult> search(CustomerSearchCriteria criteria) {

      jdbcTemplate.query("SELECT * FROM customer WHERE ID = ?",
          (rs, rowNum) ->
              new CustomerSearchResult(rs.getInt("id"), rs.getString("name")),
          "asdasd");
      // Alternative: Spring Specifications https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html
//      String jpql = "SELECT c " +
//      String jpql = "SELECT c.* " +
//      String jpql = "SELECT new victor.training.clean.domain.model.Customer(c.id, c.name)" +
      String jpql = "SELECT new victor.training.clean.application.dto.CustomerSearchResult(c.id, c.name)" +
                    " FROM Customer c " +
                    " WHERE ";
      List<String> jpqlParts = new ArrayList<>();
      jpqlParts.add("1=1"); // alternatives: Criteria API ± Spring Specifications or Query DSL
      Map<String, Object> params = new HashMap<>();

      if (criteria.name() != null) {
         jpqlParts.add("UPPER(c.name) LIKE UPPER('%' || :name || '%')");
         params.put("name", criteria.name());
      }

      if (criteria.email() != null) {
         jpqlParts.add("UPPER(c.email) = UPPER(:email)");
         params.put("email", criteria.email());
      }

      if (criteria.countryId() != null) {
         jpqlParts.add("c.country.id = :countryId");
         params.put("countryId", criteria.countryId());
      }

      String whereCriteria = join(" AND ", jpqlParts);
      var query = entityManager.createQuery(jpql + whereCriteria, CustomerSearchResult.class);
      for (String paramName : params.keySet()) {
         query.setParameter(paramName, params.get(paramName));
      }
      return query.getResultList();
   }

   //region CriteriaAPI alternative
   // add to pom <dependency>
   //            <!--Generate Criteria API metamodel in target/generated-sources/annotations-->
   //            <groupId>org.hibernate</groupId>
   //            <artifactId>hibernate-jpamodelgen</artifactId>
   //        </dependency>
//   public List<CustomerSearchResult> searchWithCriteria(CustomerSearchCriteria criteria) {
//      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//      CriteriaQuery<CustomerSearchResult> criteriaQuery = cb.createQuery(CustomerSearchResult.class);
//      Root<Customer> root = criteriaQuery.from(Customer.class);
//      criteriaQuery.select(cb.construct(CustomerSearchResult.class, root.get(Customer_.id), root.get(Customer_.name)));
//      List<Predicate> predicates = new ArrayList<>();
//      predicates.add(cb.isTrue(cb.literal(true)));
//
//      if (criteria.getName() != null) {
//         predicates.add(cb.like(cb.upper(root.get(Customer_.name)), cb.upper(cb.literal("%" + criteria.getName() + "%"))));
//      }
//
//      if (criteria.getEmail() != null) {
//         predicates.add(cb.equal(cb.upper(root.get(Customer_.email)), cb.upper(cb.literal(criteria.getEmail()))));
//      }
//
//      if (criteria.getCountryId() != null) {
//         predicates.add(cb.equal(root.get(Customer_.country).get(Country_.id), criteria.getCountryId()));
//      }
//      criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
//
//      TypedQuery<CustomerSearchResult> query = entityManager.createQuery(criteriaQuery);
//      return query.getResultList();
//   }
   //endregion

}
