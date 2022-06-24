package victor.training.clean.domain.customer.entity;

import lombok.Data;

@Data // Value Object
public class Email {
	private String to;
	private String from;
	private String subject;
	private String body;
}
