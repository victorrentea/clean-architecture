package victor.training.clean.domain.model.state.map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reflects a {@link CustomerStateMachine} into a PlantUML state diagram.
 * The graph map is the single source of truth: add a row in the example wiring
 * and re-run this {@code main}; the diagram catches up automatically.
 *
 * <p>Initial state ([*] -> X) is auto-detected as any state that has outgoing
 * edges but no incoming edges. Terminal state (X -> [*]) is the symmetric case.
 *
 * <p>Run from the repo root:
 * <pre>
 * mvn -pl clean-application exec:java \
 *   -Dexec.mainClass=victor.training.clean.domain.model.state.map.StateMachineToPlantUml
 * </pre>
 */
public class StateMachineToPlantUml {

  public static <C> String render(CustomerStateMachine<C> machine, String title) {
    Map<CustomerStatus, List<Transition<C>>> graph = machine.graph();

    Set<CustomerStatus> sources = graph.keySet();
    Set<CustomerStatus> targets = graph.values().stream()
        .flatMap(List::stream)
        .map(Transition::target)
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Set<CustomerStatus> initial = sources.stream()
        .filter(s -> !targets.contains(s))
        .collect(Collectors.toCollection(LinkedHashSet::new));
    Set<CustomerStatus> terminal = targets.stream()
        .filter(s -> !sources.contains(s))
        .collect(Collectors.toCollection(LinkedHashSet::new));

    StringBuilder sb = new StringBuilder();
    sb.append("@startuml\n");
    sb.append("title ").append(title).append("\n");
    sb.append("hide empty description\n\n");

    initial.forEach(s -> sb.append("[*] --> ").append(s).append("\n"));
    if (!initial.isEmpty()) sb.append("\n");

    graph.forEach((from, transitions) -> {
      for (Transition<C> t : transitions) {
        sb.append(from).append(" --> ").append(t.target());
        if (t.label() != null && !t.label().isEmpty()) {
          sb.append(" : ").append(t.label());
        }
        sb.append("\n");
      }
    });
    sb.append("\n");

    terminal.forEach(s -> sb.append(s).append(" --> [*]\n"));

    sb.append("@enduml\n");
    return sb.toString();
  }

  public static void main(String[] args) throws IOException {
    String puml = render(
        CustomerStateMachineExample.INSTANCE,
        "Customer lifecycle (generated from CustomerStateMachineExample)");
    Path out = Path.of("adoc/customer-state-generated.puml");
    Files.writeString(out, puml);
    System.out.println("Wrote " + out.toAbsolutePath());
  }
}
