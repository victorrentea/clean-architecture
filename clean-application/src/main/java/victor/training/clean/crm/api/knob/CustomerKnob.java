package victor.training.clean.crm.api.knob;

// a door know (data structures moving beteeen modules, not via REST)
public record CustomerKnob(long id,
                           String name,
                           String email,
                           int discountPercentage) {
}
