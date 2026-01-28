package victor.training.clean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc
public class MyOpenAPIDidNotChangeTest {
  @Autowired
  MockMvc mockMvc;

  @Value("classpath:/my-existing-openapi.json") // in src/test/resources
  Resource contractFile;

  @Test
  void my_contract_did_not_change() throws Exception {
    String contractExtractedFromCode = mockMvc.perform(get("/v3/api-docs"))
        .andReturn().getResponse().getContentAsString();

    String contractSavedOnGit = contractFile.getContentAsString(defaultCharset())
        .replace(":8080", ""); // ignoring the port

//    if (!OpenApiCompare.fromContents(contractSavedOnGit, openApiExtractedFromCode).isCompatible())
//      System.err.println(new MarkdownRender().render(diff));

    assertThat(prettifyJson(contractExtractedFromCode))
        .isEqualTo(prettifyJson(contractSavedOnGit));
//    }
  }

  private String prettifyJson(String rawJson) throws JsonProcessingException {
    if (StringUtils.isBlank(rawJson)) return rawJson;
    ObjectMapper jackson = new ObjectMapper();
    return jackson.writerWithDefaultPrettyPrinter().writeValueAsString(
            jackson.readValue(rawJson, Map.class));
  }
}
