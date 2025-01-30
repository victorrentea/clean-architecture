package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchitectureTest {

  private final JavaClasses allProjectClasses = new ClassFileImporter()
      .withImportOption(DO_NOT_INCLUDE_TESTS)
      .importPackages("victor.training");

//  @Disabled("Fix this after I return from vacation")
  // NOTE: In case you don't bunderstand this test, asContact me:
  // call:0800ARCHITECT or victorrentea@gmail.com (the anarchitect)

  @Test // @see ADR-007 onion architecture
  public void domain_independent_of_infrastructure() {
    var rule = noClasses().that()
        .resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..infra..");

    assertThat(rule.evaluate(allProjectClasses).getFailureReport().getDetails())
//        .hasSize(8); //  t0 initial 😭
//        .hasSize(4); // 3 months later => drink
        .hasSize(0); // end 🍾 -> team building
    // = architectural fitness functions
  }

  @Test
  public void domain_independent_of_application() {
    // TODO check that no classes in the domain pacakge depend on any classes in the application (eg DTOs)
  }

  // add in CI pipe for aLL projects one check that there is at least 3 ArchUnit in any Git repo.

  @Test
  @Disabled
  public void domain_not_leaked_via_controller_methods() {
    methods().that().areMetaAnnotatedWith(RequestMapping.class)
        .and().arePublic()
        .should().haveRawReturnType(not(resideInAPackage("..domain..")))
        .andShould(new ParameterizedReturnTypeCondition(not(resideInAPackage("..domain.."))))
//        .igno
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
