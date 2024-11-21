package victor.training.clean.application.dto;

import lombok.Builder;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import java.util.Optional;

@Builder
public record IoNuStiu(String name, Optional<String> phone) {

}

class Undeva {
  public static void main(String[] args) {
    IoNuStiu ioi = IoNuStiu.builder()
        .name("OMG")
//        .phone(Optional.of("phone"))
        .build();
    System.out.println(ioi);
    System.out.println(ioi.phone().map(String::toUpperCase).orElse("N/A"));

  }
}