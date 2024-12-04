package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchitectureTest {

  private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("victor.training");

  // use the CODEOWNERS file to request a PR review to the arch/lead on any change of this file
  @Test
  public void domain_independent_of_infrastructure() {
    ClassesShouldConjunction rule = noClasses().that()
        .resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..infra..");
    rule.check(allProjectClasses);

    assertThat(rule.evaluate(allProjectClasses).getFailureReport().getDetails())
        .hasSize(0); // end 🍾
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


  //A) ArchUnit rule: controller should not access repository
  @Test
  public void controllerShouldNotAccessRepository() {
    noClasses().that()
        .areAnnotatedWith(RestController.class)
        // .resideInAPackage("..controller..")
        .should().dependOnClassesThat().resideInAPackage("..repo..")
        .check(allProjectClasses);
  }
  //B) ArchUnit rule: repository should not use DTOs
  @Test
  public void repositoryShouldNotUseDTOs() {
    noClasses().that().resideInAPackage("..repo..")
        .should().dependOnClassesThat().resideInAPackage("..dto..")
        .check(allProjectClasses);
  }
  //C) ArchUnit rule: repository should not use domain.service
  @Test
  public void repositoryShouldNotUseDomainService() {
    noClasses().that().resideInAPackage("..repo..")
        .should().dependOnClassesThat().resideInAPackage("..service..")
        .check(allProjectClasses);
  }
  //D) ArchUnit rule: no @Entity (jpa) should be annotated with @Data (lombok)
  @Test
  public void noEntityShouldBeAnnotatedWithData() {
    noClasses().that().areAnnotatedWith("jakarta.persistence.Entity")
        .should().beAnnotatedWith("lombok.Data")
        .check(allProjectClasses);
  }

  //D) ArchUnit rule: no @Entity (jpa) should not override hashCode
//  @Test
//  public void noEntityShouldOverrideHashCode() {
//    noClasses().that().areAnnotatedWith("jakarta.persistence.Entity")
//        .should().haveMethod("hashCode")
//        .check(allProjectClasses);
//  }

  //E) ArchUnit rule: domain should not use DTOs
  @Test
  public void domainShouldNotUseDTOs() {
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..dto..")
        .check(allProjectClasses);
  }

}
