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
import victor.training.clean.shared.domain.model.Customer;
import victor.training.clean.shared.domain.model.Site;
import victor.training.clean.application.dto.CustomerDto;
import victor.training.clean.application.dto.CustomerDto.CustomerDtoBuilder;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.shared.domain.repo.CustomerRepo;
import victor.training.clean.shared.domain.repo.SiteRepo;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
@Transactional
public class RegisterCustomerMvcTest {
    private static final ObjectMapper jackson = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SiteRepo siteRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @MockBean
    private EmailSender emailSender;

    private Site site;
    private CustomerDtoBuilder requestDto;

    @BeforeEach
    public final void before() {
        site = siteRepo.save(new Site());
        requestDto = CustomerDto.builder()
                .email("::email::")
                .name("::name::")
                .siteId(site.getId());
    }

    @Test
    void register_and_get() throws Exception {

        register(requestDto.build()).andExpect(status().isOk());

        assertThat(customerRepo.findAll()).hasSize(1);
        Customer customer = customerRepo.findAll().get(0);
        assertThat(customer.getName()).isEqualTo("::name::");
        assertThat(customer.getEmail()).isEqualTo("::email::");
        assertThat(customer.getSite().getId()).isEqualTo(site.getId());
        verify(emailSender).sendEmail(argThat(email -> email.getTo().equals("::email::")));


        CustomerDto responseDto = getCustomer(customer.getId());
        assertThat(responseDto.getId()).isEqualTo(customer.getId());
        assertThat(responseDto.getName()).isEqualTo("::name::");
        assertThat(responseDto.getEmail()).isEqualTo("::email::");
        assertThat(responseDto.getSiteId()).isEqualTo(site.getId());
        assertThat(responseDto.getCreationDateStr()).isEqualTo(now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }

    @Test
    void nameTooShortThrows() throws Exception {
        requestDto.name("1");

        register(requestDto.build()).andExpect(status().isInternalServerError());
    }

    @Test
    void existingEmailFails() throws Exception {
        customerRepo.save(new Customer().setEmail("::email::"));

        register(requestDto.build())
                .andExpect(status().isInternalServerError())
        //          .andExpect(status().is4xxClientError())
        //          .andExpect(content().string("Customer email is already registered"))
        ;
    }

    private ResultActions register(CustomerDto requestDto) throws Exception {
        return mockMvc.perform(post("/customer")
                .contentType(APPLICATION_JSON)
                .content(jackson.writeValueAsString(requestDto))
        );
    }

    private CustomerDto getCustomer(long customerId) throws Exception {
        String responseString = mockMvc.perform(get("/customer/{id}", customerId)
                .accept(APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
        System.out.println("Got response string: " + responseString);
        CustomerDto dto = jackson.readValue(responseString, CustomerDto.class);
        return dto;
    }
}
