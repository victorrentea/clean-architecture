package victor.training.clean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import victor.training.clean.application.dto.CustomerSearchResult;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerDto.CustomerDtoBuilder;
import victor.training.clean.domain.repo.CountryRepo;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
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
        customerRepo.deleteAll();
    }

    private CustomerDtoBuilder registerRequest() {
        return CustomerDto.builder()
            .email(CUSTOMER_EMAIL)
            .name("::name::")
            .countryId(country.getId());
    }

    @Test
    void register_and_get_and_search() throws Exception {

        register(registerRequest()).andExpect(status().isOk());

        assertThat(customerRepo.findAll()).hasSize(1);
        Customer customer = customerRepo.findAll().get(0);
        assertThat(customer.getName()).isEqualTo("::name::");
        assertThat(customer.getEmail()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(customer.getCountry().getId()).isEqualTo(country.getId());
        verify(emailSender).sendEmail(argThat(email -> email.getTo().equals(CUSTOMER_EMAIL)));


        CustomerDto responseDto = getCustomer(customer.getId());

        assertThat(responseDto.id()).isEqualTo(customer.getId());
        assertThat(responseDto.name()).isEqualTo("::name::");
        assertThat(responseDto.email()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(responseDto.countryId()).isEqualTo(country.getId());
        assertThat(responseDto.createdDateStr()).isEqualTo(now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        assertThat(search("ame")).hasSize(1);
    }

    @Test
    void get_returns404_ifNotFound() throws Exception {
        mockMvc.perform(get("/customers/{id}", 99999)
            .accept(APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void nameTooShortThrows() throws Exception {
        register(registerRequest().name("1")).andExpect(status().isInternalServerError());
    }

    @Test
    void twoCustomerWithSameEmail() throws Exception {
        register(registerRequest().email(CUSTOMER_EMAIL))
            .andExpect(status().isOk());

        register(registerRequest().email(CUSTOMER_EMAIL))
            .andExpect(status().isInternalServerError())
        //          .andExpect(status().is4xxClientError())
        //          .andExpect(content().string("Customer email is already registered"))
        ;
    }

    private ResultActions register(CustomerDtoBuilder requestDto) throws Exception {
        return mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(jackson.writeValueAsString(requestDto.build()))
        );
    }
    private List<CustomerSearchResult> search(String name) throws Exception {
        String responseJson = mockMvc.perform(post("/customers/search")
                .contentType(APPLICATION_JSON)
                .content(String.format("{\"name\": \"%s\"}\n", name)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return List.of(jackson.readValue(responseJson, CustomerSearchResult[].class)); // trick to unmarshall a collection<obj>
    }

    private CustomerDto getCustomer(long customerId) throws Exception {
        String responseString = mockMvc.perform(get("/customers/{id}", customerId)
                .accept(APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
        return jackson.readValue(responseString, CustomerDto.class);
    }
}
