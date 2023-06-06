package victor.training.clean.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class Email {
	String to;
	String from;
	String subject;
	String body;
}
