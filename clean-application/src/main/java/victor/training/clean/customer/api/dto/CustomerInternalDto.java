package victor.training.clean.customer.api.dto;

import lombok.Data;

@Data
public class CustomerInternalDto {
    private final Long id;
    private final String name;

    public CustomerInternalDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
