package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Disabled("Fix this after I return from vacation")
// NOTE: In case you don't understand this test, contact me:
// call:0800ARCHITECT or victorrentea@gmail.com (the anarchitect)
public class ArchitectureTest {

  private final JavaClasses allProjectClasses = new ClassFileImporter()
      .withImportOption(DO_NOT_INCLUDE_TESTS)
      .importPackages("victor.training"); // TODO adjust

  @Test // as per ADR-005
  //@Disabled("Fix when return from vacation")
  public void domain_independent_of_infrastructure() {
    var rule = noClasses().that()
        .resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..infra..");
    List<String> failures = rule.evaluate(allProjectClasses).getFailureReport().getDetails();

//    int expectedFailureCount = 8; //  initial 😭 < at least it doesn't get worse
//    int expectedFailureCount = 6; //  3 months later 🍺
//    int expectedFailureCount = 0; // end 🍾🍺🍺🍺🍺🍺
//    assertEquals(expectedFailureCount, failures.size(), String.join("\n", failures));

    FreezingArchRule.freeze(rule).check(allProjectClasses);
    // it would make sure no NEW violations are found
    // compared to the ones saved on Git yesterday
    // this set of known violations is auto-created in a folder in your project
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

}
