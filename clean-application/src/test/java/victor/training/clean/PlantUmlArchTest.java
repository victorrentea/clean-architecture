package victor.training.clean;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The picture IS the rule: docs/packages.puml is checked against the bytecode on every build,
 * so a diagram that drifts from the code fails the build instead of quietly lying.
 */
@AnalyzeClasses(packages = "victor.training.clean", importOptions = DoNotIncludeTests.class)
class PlantUmlArchTest {

  private static final Path DIAGRAM = Paths.get("docs/packages.puml");
  private static final Path SOURCE_ROOT = Paths.get("src/main/java/victor/training/clean");

  /** The OpenAPI-generated HTTP plumbing is not part of the architecture we designed. */
  private static final String GENERATED_CLIENT = "..clean.auth..";
  /** The root package only holds the Spring Boot entry point and the generated ApiClient. */
  private static final String ROOT_PACKAGE = "victor.training.clean";

  /** No dependency between packages may exist unless an arrow in the diagram allows it. */
  @ArchTest
  static final ArchRule adheresToDiagram = classes()
      .that().resideOutsideOfPackage(GENERATED_CLIENT)
      .and().resideOutsideOfPackage(ROOT_PACKAGE)
      .should(adhereToPlantUmlDiagram(DIAGRAM, consideringOnlyDependenciesInDiagram()));

  /** ... and no package may hide from the diagram: every package of code must be drawn. */
  @Test
  void diagramPackagesMatchCodePackages() throws IOException {
    assertThat(parsePackagesFromDiagram())
        .as("packages.puml stereotypes must match the actual packages under victor.training.clean")
        .isEqualTo(listCodePackages());
  }

  private static Set<String> parsePackagesFromDiagram() throws IOException {
    Matcher matcher = Pattern.compile("<<\\.\\.([a-zA-Z0-9.]+)>>").matcher(Files.readString(DIAGRAM));
    Set<String> result = new TreeSet<>();
    while (matcher.find()) {
      result.add(matcher.group(1).replaceFirst("^clean\\.?", ""));
    }
    return result;
  }

  private static Set<String> listCodePackages() throws IOException {
    try (Stream<Path> paths = Files.walk(SOURCE_ROOT)) {
      return paths.filter(Files::isDirectory)
          .filter(dir -> !dir.equals(SOURCE_ROOT))
          .filter(PlantUmlArchTest::containsJavaFile)
          .map(dir -> SOURCE_ROOT.relativize(dir).toString().replace('/', '.'))
          .collect(Collectors.toCollection(TreeSet::new));
    }
  }

  private static boolean containsJavaFile(Path dir) {
    try (Stream<Path> entries = Files.list(dir)) {
      return entries.anyMatch(p -> p.toString().endsWith(".java"));
    } catch (IOException e) {
      return false;
    }
  }
}
