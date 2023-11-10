package victor.training.clean.application.entity;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class Email {
	String to;
	List<String> cc = new ArrayList<>();
	String from;
	String subject;
	String body;
}
