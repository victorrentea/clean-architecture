package victor.training.clean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.common.country.Country;
import victor.training.clean.common.country.CountryRepo;
import victor.training.clean.crm.aplication.usecase.GetCustomerByIdUseCase.GetCustomerByIdResponse;
import victor.training.clean.crm.aplication.usecase.RegisterCustomerUseCase.RegisterCustomerRequest;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;
import victor.training.clean.notification.EmailSender;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
@Transactional
public class LargeIntegrationTest {
  private static final ObjectMapper jackson = new ObjectMapper();
  public static final String CUSTOMER_EMAIL = "a@b.com";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CountryRepo countryRepo;
  @Autowired
  private CustomerRepo customerRepo;
  @MockBean
  private EmailSender emailSender;

  private Country country;

  @BeforeEach
  public final void before() {
    country = countryRepo.save(new Country());
  }

  @Test
  void findById_returns404_ifNotFound() throws Exception {
    mockMvc.perform(get("/customer/{id}", 99999)
        .accept(APPLICATION_JSON)
    ).andExpect(status().isNotFound());
  }

  @Test
  void register_and_get_and_search() throws Exception {
    register(registerCustomerRequest()
        .build()).andExpect(status().isOk());

    assertThat(customerRepo.findAll()).hasSize(1);
    Customer customer = customerRepo.findAll().get(0);
    assertThat(customer.getName()).isEqualTo("::name::");
    assertThat(customer.getEmail()).isEqualTo(CUSTOMER_EMAIL);
    assertThat(customer.getCountryId()).isEqualTo(country.getId());
    verify(emailSender).sendEmail(argThat(email -> email.getTo().equals(CUSTOMER_EMAIL)));


    GetCustomerByIdResponse responseDto = getCustomer(customer.getId());

    assertThat(responseDto.getId()).isEqualTo(customer.getId());
    assertThat(responseDto.getName()).isEqualTo("::name::");
    assertThat(responseDto.getEmail()).isEqualTo(CUSTOMER_EMAIL);
    assertThat(responseDto.getCountryId()).isEqualTo(country.getId());
    assertThat(responseDto.getCreationDateStr()).isEqualTo(now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    search("ame", 1);
  }

  @Test
  void nameTooShortThrows() throws Exception {
    register(registerCustomerRequest().name("1").build()).andExpect(status().isInternalServerError());
  }

  @Test
  void existingEmailFails() throws Exception {

    register(registerCustomerRequest().build()).andExpect(status().isOk());

    register(registerCustomerRequest().build()).andExpect(status().isInternalServerError());
  }

  private RegisterCustomerRequest.RegisterCustomerRequestBuilder registerCustomerRequest() {
    return RegisterCustomerRequest.builder()
        .email(CUSTOMER_EMAIL)
        .name("::name::")
        .countryId(country.getId());
  }

  private ResultActions register(RegisterCustomerRequest requestDto) throws Exception {
    return mockMvc.perform(post("/customer")
        .contentType(APPLICATION_JSON)
        .content(jackson.writeValueAsString(requestDto))
    );
  }

  private void search(String name, int expectedNumberOfResults) throws Exception {
    mockMvc.perform(post("/customer/search")
            .contentType(APPLICATION_JSON)
            .content(String.format("{\"name\": \"%s\"}\n", name)))
        .andExpect(jsonPath("$", hasSize(expectedNumberOfResults)));
  }

  private GetCustomerByIdResponse getCustomer(long customerId) throws Exception {
    String responseString = mockMvc.perform(get("/customer/{id}", customerId)
        .accept(APPLICATION_JSON)
    ).andReturn().getResponse().getContentAsString();
    return jackson.readValue(responseString, GetCustomerByIdResponse.class);
  }
}
