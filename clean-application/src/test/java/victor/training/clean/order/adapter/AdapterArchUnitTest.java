package victor.training.clean.order.adapter;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AdapterArchUnitTest {
   @Test
   public void dependencyInversionTest() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");
// if tests fails, contact victorrentea@gmail.com
      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..infra..")
          .check(classes);
   }
}
