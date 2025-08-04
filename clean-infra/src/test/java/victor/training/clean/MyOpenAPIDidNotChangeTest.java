package victor.training.clean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.openapitools.openapidiff.core.output.MarkdownRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
public class MyOpenAPIDidNotChangeTest {
  @Autowired
  MockMvc mockMvc;

  @Value("classpath:/my-existing-openapi.json") // extracted yesterday
  Resource myExpectedOpenAPI;

  @Test
  void my_contract_did_not_change() throws Exception {
    String currentOpenAPI = mockMvc.perform(get("/v3/api-docs"))
        .andReturn().getResponse().getContentAsString();

    String expectedOpenAPI = myExpectedOpenAPI.getContentAsString(defaultCharset())
        .replace(":8080", ""); // ignore the port

//    if (!OpenApiCompare.fromContents(expectedOpenAPI, currentOpenAPI).isCompatible()) {
//      System.err.println(new MarkdownRender().render(diff));

    assertThat(prettify(currentOpenAPI))
        .describedAs("Exposed OpenAPI should not have changed")
        .isEqualTo(prettify(expectedOpenAPI));
//    }
  }

  private String prettify(String rawJson) throws JsonProcessingException {
    if (StringUtils.isBlank(rawJson)) return rawJson;
    ObjectMapper jackson = new ObjectMapper();
    return jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
        jackson.readValue(rawJson, Map.class));
  }
}
