package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {

   private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("victor.training");

   @Test
   // NOTE: In case you don't understand this test, contact me:
   // +40720019564 or victorrentea@gmail.com (the anarchitect)
   public void domain_independent_of_infrastructure() {
      noClasses().that().resideInAPackage("..domain..")
          .should().dependOnClassesThat().resideInAPackage("..infra..")
          .check(allProjectClasses);
   }

   @Test
   public void service_independent_of_facade() {
      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..facade..")
          .check(allProjectClasses);
   }

   @Test
   public void domain_not_exposed_via_controller_methods() {
      methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
          .and().arePublic()
          .should().haveRawReturnType(resideOutsideOfPackage("..entity.."))
          .check(allProjectClasses);
   }
}
