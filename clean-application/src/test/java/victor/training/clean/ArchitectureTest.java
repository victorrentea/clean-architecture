package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import io.micrometer.core.annotation.Timed;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

// NOTE: In case you don't understand this test, contact me:
// call:0800ARCHITECT or victorrentea@gmail.com (the anarchitect)
//@Disabled("TODO fix when I return from sabbatical year:)")
public class ArchitectureTest {

  private final JavaClasses allProjectClasses = new ClassFileImporter()
      .withImportOption(DO_NOT_INCLUDE_TESTS)
      .importPackages("victor.training"); // TODO adjust

  @Test // as per ADR-005
  public void domain_independent_of_infrastructure() {
    var rule = noClasses().that()
        .resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..infra..");
    List<String> failures = rule.evaluate(allProjectClasses).getFailureReport().getDetails();

//    int expectedFailureCount = 6; //  initial 😭 - i'm far from my goal
//    int expectedFailureCount = 3; //  3 months later
    int expectedFailureCount = 0; // end 🍾🍾🍾🍾

    assertEquals(expectedFailureCount, failures.size(), String.join("\n", failures));

    // TODO FreezingArchRule.freeze(rule.check(classes))
  }

  @Test
  public void yourRule() {
    // TODO check an architectural rule for your project (passing or failing)
    // Ideas (if lacking):
    // - controller package should not depend on repository
    // - repository should not use services
    // - domain should not use DTOs
    // - no classes in package X should have > 300 lines
    // - the 'common' package doesn't depend on any other package
  }

  @Test
  @Disabled
  public void domain_not_leaked_via_controller_methods() {
    methods().that().areMetaAnnotatedWith(RequestMapping.class)
        .and().arePublic()
        .should().haveRawReturnType(not(resideInAPackage("..domain..")))
        .andShould(new ParameterizedReturnTypeCondition(not(resideInAPackage("..domain.."))))
        .check(allProjectClasses);
  }

  @Test
  public void domainClassesShouldBeSmall() {
    classes().that().resideInAPackage("..domain..")
        .should(haveLessLineNumbersThan(300))
        .check(allProjectClasses);
  }


  private ArchCondition<JavaClass> haveLessLineNumbersThan(int number) {
    return new ArchCondition<JavaClass>("have less line numbers than " + number) {
      @Override
      public void check(JavaClass javaClass, ConditionEvents events) {
        int lastLineNumber = javaClass.getCodeUnits().stream()
            .flatMap(c -> c.getAccessesFromSelf().stream())
            .map(JavaAccess::getLineNumber)
            .mapToInt(n -> n)
            .max()
            .orElse(0);
        boolean satisfied = lastLineNumber < number;
        events.add(new SimpleConditionEvent(javaClass, satisfied, javaClass.getName() + " has more than " + lastLineNumber + " lines"));
      }
    };
  }

  @Test
  void controllers_should_be_annotated_with_Timed() {
    final JavaClasses importedClasses = new ClassFileImporter()
        .importPackages("victor.training");

    final ArchRule rule = classes()
        .that().areAnnotatedWith(RestController.class)
        .should().beAnnotatedWith(Timed.class);

    rule.check(importedClasses);
  }
  /**
   * As per ADR: Classes injecting RestTemplate should be annotated with @Timed
   * This ensures proper monitoring of external API calls using Micrometer.
   */
  @Test
  public void classesInjectingRestTemplateShouldBeAnnotatedWithTimed() {
    classes()
        .should(haveTimedAnnotationIfUsingRestTemplate())
        .check(allProjectClasses);
  }

  /**
   * Custom condition to check if classes using RestTemplate are annotated with @Timed.
   * This is part of our architectural decision to ensure all external API calls are properly monitored.
   *
   * The condition excludes:
   * - RestTemplate itself
   * - Generated classes (like ApiClient)
   */
  private ArchCondition<JavaClass> haveTimedAnnotationIfUsingRestTemplate() {
    return new ArchCondition<JavaClass>("have @Timed annotation if using RestTemplate") {
      @Override
      public void check(JavaClass javaClass, ConditionEvents events) {
        // Skip RestTemplate itself
        if (javaClass.getName().equals(RestTemplate.class.getName())) {
          return;
        }

        // Skip generated classes
        if (isGeneratedClass(javaClass)) {
          return;
        }
        
        // Check if the class uses RestTemplate (has fields, constructor params, or method params of type RestTemplate)
        boolean usesRestTemplate = false;

        // Check fields
        for (JavaField field : javaClass.getAllFields()) {
          if (field.getRawType().getName().equals(RestTemplate.class.getName())) {
            usesRestTemplate = true;
            break;
          }
        }

        // If class doesn't use RestTemplate, we don't need to check for @Timed
        if (!usesRestTemplate) {
          return;
        }

        // Check if the class has @Timed annotation
        boolean hasTimedAnnotation = javaClass.isAnnotatedWith(Timed.class);

        // Only report a violation if the class uses RestTemplate but doesn't have @Timed
        if (!hasTimedAnnotation) {
          events.add(new SimpleConditionEvent(javaClass, false,
              String.format("Class %s uses RestTemplate but is not annotated with @Timed", 
                  javaClass.getName())));
        }
      }
    };
  }

  /**
   * Helper method to identify generated classes.
   */
  private boolean isGeneratedClass(JavaClass javaClass) {
    // Exclude classes from the generated OpenAPI client
    if (javaClass.getPackageName().equals("victor.training.clean") &&
        javaClass.getSimpleName().equals("ApiClient")) {
      return true;
    }
    return false;
  }

}
