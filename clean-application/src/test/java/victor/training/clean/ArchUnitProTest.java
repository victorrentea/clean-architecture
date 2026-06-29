package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaParameterizedType;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "victor.training.clean",
    importOptions = DoNotIncludeTests.class)
public class ArchUnitProTest {

  @ArchTest
  void domain_model_independent_of_application(JavaClasses classes) {
    ArchRuleDefinition.noClasses().that()
        .resideInAPackage("..domain.model..")
        .should().dependOnClassesThat()
        .resideInAPackage("..application..")
        .check(classes);
  }

  @ArchTest
  void controller_dont_use_repos(JavaClasses classes) {
    FreezingArchRule.freeze(
            ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAPackage("..repo.."))
        .check(classes);
  }

  // ⭐️ as per ADR-007: a search must SELECT a DTO/projection, never an @Entity.
  @ArchTest
  void search_methods_must_not_return_entities(JavaClasses classes) {
    methods().that()
        .areDeclaredInClassesThat().resideInAPackage("..repo..")
        .and().haveNameMatching("(?i).*search.*")
        .should(notReturnAnEntity())
        .check(classes);
  }

  private static final String ENTITY_ANNOTATION = "jakarta.persistence.Entity";

  /** Flags a method that returns an @Entity, directly or as a List<@Entity>. */
  private static ArchCondition<JavaMethod> notReturnAnEntity() {
    return new ArchCondition<>("not return an entity or a list of entities") {
      @Override
      public void check(JavaMethod method, ConditionEvents events) {
        List<JavaClass> returnedTypes = new ArrayList<>();
        returnedTypes.add(method.getRawReturnType());          // Customer, or List for List<Customer>
        if (method.getReturnType() instanceof JavaParameterizedType parameterized) {
          parameterized.getActualTypeArguments()               // the Customer in List<Customer>
              .forEach(arg -> returnedTypes.add(arg.toErasure()));
        }
        for (JavaClass type : returnedTypes) {
          if (type.isAnnotatedWith(ENTITY_ANNOTATION)) {
            events.add(SimpleConditionEvent.violated(method,
                method.getFullName() + " returns @Entity " + type.getName()
                + " — a search must SELECT a DTO/projection instead (ADR-007)"));
          }
        }
      }
    };
  }

  @ArchTest
  @Disabled
  void layers(JavaClasses classes) {
    layeredArchitecture()
        .consideringAllDependencies()
        .layer("Controller").definedBy("..controller..")
        .layer("Service").definedBy("..service..")
        .layer("Persistence").definedBy("..persistence..")

        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");
  }

}
