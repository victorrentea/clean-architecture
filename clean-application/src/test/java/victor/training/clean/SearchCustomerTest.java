//package victor.training.clean;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;
//import victor.training.clean.insurance.domain.repo.Country;
//import victor.training.clean.crm.domain.model.Customer;
//import victor.training.clean.insurance.domain.repo.CountryRepo;
//import victor.training.clean.crm.domain.repo.CustomerRepo;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@SpringBootTest
//@Transactional
//@ActiveProfiles("db-mem")
//@AutoConfigureMockMvc
//public class SearchCustomerTest {
//  private final static ObjectMapper jackson = new ObjectMapper();
//  @Autowired
//  MockMvc mockMvc;
//  @Autowired
//  CustomerRepo customerRepo;
//  @Autowired
//  CountryRepo countryRepo;
//
//  private long countryId;
//
//  @BeforeEach
//  final void before() {
//    assertThat(customerRepo.findAll()).isEmpty();
//    Country country = countryRepo.save(new Country());
//    countryId = country.getId();
//    customerRepo.save(new Customer()
//        .setName("John")
//        .setEmail("a@B.com")
//        .setCountry(country));
//
//  }
//  private static CustomerSearchCriteriaBuilder criteriaWith() {
//    return builder();
//  }
//  @Test
//  void byNameLikeMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().name("oH"))).isNotEmpty();
//  }
//  @Test
//  void byNameLikeNoMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().name("xyz"))).isEmpty();
//  }
//  @Test
//  void byEmailMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().email("A@b.coM"))).isNotEmpty();
//  }
//  @Test
//  void byEmailNoMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().email("x@y.z"))).isEmpty();
//  }
//  @Test
//  void byCountryMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().countryId(countryId))).isNotEmpty();
//  }
//  @Test
//  void byCountryNoMatch() throws Exception {
//    assertThat(searchAPI(criteriaWith().countryId(-1L))).isEmpty();
//  }
//
//  private List<CustomerSearchResult> searchAPI(CustomerSearchCriteriaBuilder searchCriteria) throws Exception {
//    String requestJson = jackson.writeValueAsString(searchCriteria.build());
//    String responseJson = mockMvc.perform(post("/customer/search")
//                    .contentType("application/json")
//                    .content(requestJson))
//            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
//            .andReturn()
//            .getResponse()
//            .getContentAsString();
//    List<CustomerSearchResult> result = jackson.readValue(responseJson, new TypeReference<>() {
//    });
//    return result;
//  }
//}
