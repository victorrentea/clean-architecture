package victor.training.clean.application.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// the model of a row in the search results grid displayed in UI
public record CustomerSearchResult( // JSON
    long id,
    String name) {

  @JsonProperty
  public String getNameCamel() {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }
}

