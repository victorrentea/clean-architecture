package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {
   @Test
   public void service_independent_of_infrastructure() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      noClasses().that().resideInAPackage("..service..")
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
}
