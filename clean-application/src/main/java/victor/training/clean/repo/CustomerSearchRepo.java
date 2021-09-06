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
   // Idea1: Tolerate Eventual Consistency: The client won't expect to see up-to-date totals. Yesterday ones are good enough. Materialized Views
   // you create a view that STORES its content. You update that view every night. Updating the view will QUERY the Events table

   // Idea2: keep the column; how to update it ?
   // a) when INSERT order >> do UPDATE Customer > Coupling
   // b) when INSERT order >> fire OrderPlacedEvent >> listend to by CustomerTotalListener >> better suited for MS (messages on queues)
   // c) Use kafka connect to listen to database changes directly and fire event from outside of your app.

   // Idea3: ES

   // idea4: UX trick :the total is still loading in UI - N/A for Massive reads

   // idea5: if we are talking about 1000/sec reads INSANE reads and not 1-2 columns but multiple
   // CQRS >> design 2 systems: one to handle WRITEs the other for READS >> you get to model manually a different READ model than WRITE


   public List<CustomerSearchResult> search(CustomerSearchCriteria criteria) {
      String jpql = "SELECT new victor.training.clean.facade.dto.CustomerSearchResult(c.id, c.name, c.totalOrderAmount)" +
                    " FROM Customer c " +
                    " WHERE 1=1 ";

      Map<String, Object> paramMap = new HashMap<>();

      if (StringUtils.isNotEmpty(criteria.name)) {
         jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
         paramMap.put("name", criteria.name);
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
