package victor.training.clean.customer.entity;

import lombok.Data;

@Data
public class Email { // VO
	private String to;
	private String from;
	private String subject;
	private String body;
}
