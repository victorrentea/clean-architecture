package victor.training.clean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
public class MyOpenAPIDidNotChangeTest {
  @Autowired
  MockMvc mockMvc;
  ObjectMapper jackson = new ObjectMapper();

  @Value("classpath:/my-existing-openapi.json") // extracted from deploy
  Resource myExpectedOpenAPI;

  @Test /// this test is gonna fail for any accidental change in my contract for any breaking change
  void my_contract_did_not_change() throws Exception {
    String actualOpenAPIJson = prettifyJsonString(
            mockMvc.perform(get("/v3/api-docs"))
                    .andReturn().getResponse().getContentAsString());

    String expectedOpenAPIJson = prettifyJsonString(
            IOUtils.toString(myExpectedOpenAPI.getInputStream())
                    .replace(":8080", "")); // hack the extracted port


    System.out.println("New contract: " + actualOpenAPIJson);
    ChangedOpenApi diff = OpenApiCompare.fromContents(expectedOpenAPIJson, actualOpenAPIJson);

    if (!diff.isCompatible()) {
      String render = new MarkdownRender().render(diff);
      System.err.println(render);

      assertThat(actualOpenAPIJson)
          .describedAs("Exposed OpenAPI should not have changed")
          .isEqualTo(expectedOpenAPIJson);
    }
  }

  private String prettifyJsonString(String rawJson) throws JsonProcessingException {
      return StringUtils.isBlank(rawJson) ? rawJson :
              jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
                      jackson.readValue(rawJson, Map.class));
  }
}
