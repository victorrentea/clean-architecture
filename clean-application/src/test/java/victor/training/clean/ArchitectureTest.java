package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchitectureTest {

  private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("victor.training");

//  @Disabled("Fix this after I return from vacation")
  // NOTE: In case you don't understand this test, contact me:
  // call:0800ARCHITECT or victorrentea@gmail.com (the anarchitect)
  @Test
  public void domain_independent_of_infrastructure() {
    ClassesShouldConjunction rule = noClasses().that()
        .resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..infra..");

    assertThat(rule.evaluate(allProjectClasses).getFailureReport().getDetails())
//        .hasSize(8); // end
        .hasSize(4); // end 🍾
  }

  @Test
  public void domain_independent_of_application() {
    // TODO check that no classes in the domain pacakge depend on any classes in the application (eg DTOs)
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

}
