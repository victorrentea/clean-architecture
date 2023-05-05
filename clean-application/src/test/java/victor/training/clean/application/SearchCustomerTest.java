package victor.training.clean.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.application.dto.CustomerSearchCriteria;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static victor.training.clean.application.dto.CustomerSearchCriteria.builder;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class SearchCustomerTest {
  private final static ObjectMapper jackson = new ObjectMapper();
  @Autowired
  MockMvc mockMvc;
  @Autowired
  CustomerRepo customerRepo;

  Customer customer;

  @BeforeEach
  final void before() {

    customer = customerRepo.save(new Customer("John"));
  }
  @Test
  void byNameLike() throws Exception {
    String requestJson = jackson.writeValueAsString(builder().name("oH").build());
    String responseJson = mockMvc.perform(post("/customer/search")
                    .contentType("application/json")
                    .content(requestJson))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    List<CustomerSearchResult> result = jackson.readValue(responseJson,  new TypeReference<List<CustomerSearchResult>>() { });

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(customer.getId());
  }
}
