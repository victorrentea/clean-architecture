package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AnalyzeClasses(packages = "victor.training.clean",
    importOptions = DoNotIncludeTests.class)
public class ArchUnitProTest {

  @ArchTest
  void domain_model_independent_of_application(JavaClasses classes) {
    FreezingArchRule.freeze(ArchRuleDefinition.noClasses().that()
            .resideInAPackage("..domain...")
        .should().dependOnClassesThat()
            .resideInAPackage("..application.."))
        .check(classes);
  }


  @ArchTest // as per ADR-005
  public void domain_independent_of_infrastructure(JavaClasses classes) {
    FreezingArchRule.freeze(noClasses().that()
            .resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infra.."))
        .check(classes);
  }

  @ArchTest
  void controller_dont_use_repos(JavaClasses classes) {
    FreezingArchRule.freeze(
            ArchRuleDefinition.noClasses().that()
                .resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAPackage("..repo.."))
        .check(classes);
  }

  @ArchTest
  @Disabled
  void layers(JavaClasses classes) {
    layeredArchitecture()
        .consideringAllDependencies()
        .layer("Controller").definedBy("..controller..")
        .layer("Service").definedBy("..service..")
        .layer("Persistence").definedBy("..persistence..")

        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");
  }

}
