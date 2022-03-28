package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {
   @Test
//   @Disabled
   public void service_independent_of_infrastructure() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      noClasses().that().resideInAPackage("..domain..")
          .should().dependOnClassesThat().resideInAPackage("..infra..")
          .check(classes);
   }

   @Test
   public void service_independent_of_facade() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..facade..")
          .check(classes);
   }

   @Test
   public void domain_not_exposed_via_controller_methods() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      methods().that().areDeclaredInClassesThat().resideInAPackage("..controller..")
          .and().arePublic()
          .should().haveRawReturnType(resideOutsideOfPackage("..entity.."))
          .check(classes);
   }
}
