package victor.training.clean.insurance.api.event;

public record PolicyRequiresReevalutionEvent(String customerName, String reason) {
}
