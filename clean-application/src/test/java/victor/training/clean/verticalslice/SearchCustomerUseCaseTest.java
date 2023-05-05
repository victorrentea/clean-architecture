package victor.training.clean.verticalslice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class SearchCustomerUseCaseTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  CustomerRepo customerRepo;

  @Test
  void byNameLike() throws Exception {
    customerRepo.save(new Customer("John"));
    String responseJson = mockMvc.perform(post("/customer/search-vertical")
                    .contentType("application/json")
                    .content("""
                            {
                              "name": "oH"
                            }
                            """))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertThat(responseJson).isEqualToIgnoringWhitespace("""
            [{"id":3,"name":"John"}]""");
  }
}
