package victor.training.clean.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class Email {
	String to;
	List<String> cc = new ArrayList<>(); // any variable I ever meet of type collection
	// should never ever ever ever be nulkl.
	// OR, i will do a in-person code review with the author
	// tell Jackson/JPA to instantiate it with an empty collection
	String from;
	String subject;
	String body;
}
