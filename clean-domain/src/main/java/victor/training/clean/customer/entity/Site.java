package victor.training.clean.customer.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data // i'm sorry
public class Site {
    @Id
    private Long id;
    private String name;
}
