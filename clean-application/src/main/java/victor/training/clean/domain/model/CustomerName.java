package victor.training.clean.domain.model;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
public class CustomerName {// a value object wrapping around just the name

  //	@NotNull
  //	@NotBlank//(groups = ActivatedCustomerState.class)
  //	@Size(min = 3)
  @ValidCustomerName
  private String name;

  protected CustomerName() {} // for the eyes of Hiberante only :)

  public CustomerName(String name) {
    if (StringUtils.isBlank(name) || name.length() < 3) {
      throw new IllegalArgumentException("Customer name is not valid");
    }
    this.name = name;
    //		validate(this); // javax validator magically injected
  }

  public String getName() {
    return name;
  }
}
