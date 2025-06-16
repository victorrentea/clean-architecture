package victor.training.clean.vsa;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

import java.time.format.DateTimeFormatter;

//@RestController
public class GetCustomerByIdUseCase {
  private final CustomerRepo customerRepo;

  @java.beans.ConstructorProperties({"customerRepo"})
  public GetCustomerByIdUseCase(CustomerRepo customerRepo) {
    this.customerRepo = customerRepo;
  }

  @GetMapping("customer/{id}/vsa")
  public GetCustomerByIdResponse findById(@PathVariable long id) {
    Customer customer = customerRepo.findById(id).orElseThrow();
    return GetCustomerByIdResponse.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .siteId(customer.getCountry().getId())
        .creationDateStr(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .build();
  }

  record GetCustomerByIdResponse(
      Long id,
      String name,
      String email,
      Long siteId,
      String creationDateStr,
      boolean gold,
      String goldMemberRemovalReason) {
    public static GetCustomerByIdResponseBuilder builder() {
      return new GetCustomerByIdResponseBuilder();
    }

    public static class GetCustomerByIdResponseBuilder {
      private Long id;
      private String name;
      private String email;
      private Long siteId;
      private String creationDateStr;
      private boolean gold;
      private String goldMemberRemovalReason;

      GetCustomerByIdResponseBuilder() {
      }

      public GetCustomerByIdResponseBuilder id(Long id) {
        this.id = id;
        return this;
      }

      public GetCustomerByIdResponseBuilder name(String name) {
        this.name = name;
        return this;
      }

      public GetCustomerByIdResponseBuilder email(String email) {
        this.email = email;
        return this;
      }

      public GetCustomerByIdResponseBuilder siteId(Long siteId) {
        this.siteId = siteId;
        return this;
      }

      public GetCustomerByIdResponseBuilder creationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
        return this;
      }

      public GetCustomerByIdResponseBuilder gold(boolean gold) {
        this.gold = gold;
        return this;
      }

      public GetCustomerByIdResponseBuilder goldMemberRemovalReason(String goldMemberRemovalReason) {
        this.goldMemberRemovalReason = goldMemberRemovalReason;
        return this;
      }

      public GetCustomerByIdResponse build() {
        return new GetCustomerByIdResponse(this.id, this.name, this.email, this.siteId, this.creationDateStr, this.gold, this.goldMemberRemovalReason);
      }

      public String toString() {
        return "GetCustomerByIdUseCase.GetCustomerByIdResponse.GetCustomerByIdResponseBuilder(id=" + this.id + ", name=" + this.name + ", email=" + this.email + ", siteId=" + this.siteId + ", creationDateStr=" + this.creationDateStr + ", gold=" + this.gold + ", goldMemberRemovalReason=" + this.goldMemberRemovalReason + ")";
      }
    }
  }
}
