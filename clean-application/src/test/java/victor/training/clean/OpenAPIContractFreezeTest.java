package victor.training.clean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
public class OpenAPIContractFreezeTest {
    @Autowired
    MockMvc mockMvc;
    ObjectMapper jackson = new ObjectMapper();

    @Value("classpath:/my-openapi.json") // extracted from deploy
    Resource myExpectedOpenAPI;

    @Test
    void myOpenAPIDidNotChange() throws Exception {
        String actualOpenAPIJson = mockMvc.perform(get("/v3/api-docs"))
                .andReturn().getResponse().getContentAsString();
        String expectedOpenAPIJson = IOUtils.toString(myExpectedOpenAPI.getInputStream())
                .replace(":8080",""); // hack the extracted port
        assertThat(prettifyJsonString(actualOpenAPIJson)).isEqualTo(
                prettifyJsonString(expectedOpenAPIJson));
    }

    private String prettifyJsonString(String rawJson) throws JsonProcessingException {
        if (StringUtils.isBlank(rawJson)) {
            return rawJson;
        }
        return jackson.writerWithDefaultPrettyPrinter().writeValueAsString(jackson.readValue(rawJson, Map.class));
    }
}
