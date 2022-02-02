package victor.training.clean.order.adapter;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AdapterArchUnitTest {
   @Test
   public void dependencyInversionTest() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..infra..")
          .check(classes);
   }


   @Test
   public void domainServiceNuStieDeDtouri() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      // DACA PICA TESTUL ASTA, da un mai lui victorrentea@gmail.com (leadu) ca sa-ti explice de ce.
      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..dto..")
          .check(classes);
   }
}
