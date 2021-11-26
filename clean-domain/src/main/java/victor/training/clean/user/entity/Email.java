package victor.training.clean.user.entity;

import lombok.Data;

@Data
public class Email { // VO
	private String to;
	private String from;
	private String subject;
	private String body;
}
