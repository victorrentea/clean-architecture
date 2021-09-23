package victor.training.clean.order.adapter;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AdapterArchUnitTest {
   @Test
   public void dependencyInversionTest() {
      // My dear developer if this ever fails, please email ...  for details
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      ClassesShouldConjunction domainDoesnDependOnInfra =
          noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..infra..");

      domainDoesnDependOnInfra.check(classes);
   }
}
