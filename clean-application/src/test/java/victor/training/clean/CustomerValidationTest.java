package victor.training.clean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import victor.training.clean.application.controller.CustomerController;
import victor.training.clean.application.service.CustomerApplicationService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerValidationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CustomerApplicationService customerApplicationService;

  @Test
  void invalidFieldValuesReturnProblemJsonWithFieldPointers() throws Exception {
    mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "a",
                  "email": "not-an-email"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.type").value("https://example.net/validation-error"))
        .andExpect(jsonPath("$.title").value("Your request is not valid."))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.errors[*].pointer", hasItem("#/name")))
        .andExpect(jsonPath("$.errors[*].pointer", hasItem("#/email")));
  }

  @Test
  void crossFieldViolationReturnsRootPointer() throws Exception {
    mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "John",
                  "email": "john@example.com",
                  "shippingAddressCity": "Cluj"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.errors[*].pointer", hasItem("#/shippingAddressValid")))
        .andExpect(jsonPath("$.errors[*].detail", hasItem(containsString("Shipping address can either be fully present"))));
  }
}
