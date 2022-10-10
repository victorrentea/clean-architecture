package victor.training.clean.customer.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data // i'm sorry
public class Site {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
