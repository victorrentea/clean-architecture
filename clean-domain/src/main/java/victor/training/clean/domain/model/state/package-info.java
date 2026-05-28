/**
 * Two compilable sketches that solve the same problem as the guarded
 * transition methods on {@code Customer} ({@code validate / activate / delete}),
 * but extracted from the entity so the rules can be inspected, tested,
 * or swapped independently.
 *
 * <ul>
 *   <li>{@code map/} &mdash; table-driven: one {@code Map<from, List<Transition>>}
 *       lists every legal edge in the graph. Transitions may carry a
 *       {@link java.util.function.Predicate} guard for arbitrary rules.</li>
 *   <li>{@code gof/} &mdash; Gang-of-Four State pattern: a sealed interface
 *       plus one record per state; transitions are methods on each state.</li>
 * </ul>
 * <p>
 * Pick map/ when the graph itself is the interesting artifact (audit,
 * visualization, configuration). Pick GoF when each state carries its own
 * data or behavior (e.g. {@code ValidatedState} holds {@code validatedBy}).
 */
package victor.training.clean.domain.model.state;
