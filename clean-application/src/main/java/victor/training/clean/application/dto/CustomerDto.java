package victor.training.clean.application.dto;

import lombok.Data;
import victor.training.clean.domain.entity.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Data
public class CustomerDto {
    public Long id;
    @NotNull
    @Size(min = 5)
    public String name;
    @Email
    @NotNull
    public String email;
    public Long siteId;
    public String creationDateStr;

    public CustomerDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.siteId = customer.getSite().getId();
    }

}


// REST API : createArticle(ArticleDto1 )swagger
// DOOR API : createArticle(ArticleDto2 )

//in implem there is one Domain Service