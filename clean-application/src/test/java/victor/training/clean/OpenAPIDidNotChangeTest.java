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
public class OpenAPIDidNotChangeTest {
  @Autowired
  MockMvc mockMvc;
  ObjectMapper jackson = new ObjectMapper();

  @Value("classpath:/my-openapi.json") // extracted from deploy
  Resource myExpectedOpenAPI;

  @Test
  void my_contract_did_not_change() throws Exception {
    String actualOpenAPIJson = prettifyJsonString(
            mockMvc.perform(get("/v3/api-docs"))
                    .andReturn().getResponse().getContentAsString());

    String expectedOpenAPIJson = prettifyJsonString(
            IOUtils.toString(myExpectedOpenAPI.getInputStream())
                    .replace(":8080", "")); // hack the extracted port

    assertThat(actualOpenAPIJson).isEqualTo(expectedOpenAPIJson);
  }





  private String prettifyJsonString(String rawJson) throws JsonProcessingException {
      return StringUtils.isBlank(rawJson) ? rawJson :
              jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
                      jackson.readValue(rawJson, Map.class));
  }
}
