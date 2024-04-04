package victor.training.clean.application.dto;

// the model of a row in the search results grid displayed in UI = JSON
public record CustomerSearchResult(long id, String name) {
}

// Use-Case Optimized Query pattern in DDD