package victor.training.clean.order.adapter;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class AdapterArchUnitTest {
   @Test
   public void dependencyInversionTest() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      ClassesShouldConjunction domainDoesnDependOnInfra =
          noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..infra..");

      // DACA PICA TESTU ASTA SI NU STII DE CE, da mai la arhitectu@turnu-fildes.com sa-l intrebi
      domainDoesnDependOnInfra.check(classes);
   }
}
