package victor.training.clean;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "victor", importOptions = ImportOption.DoNotIncludeTests.class)
class NoDeprecatedDependencyTest {

  @ArchTest
  static final ArchRule noClassesShouldDependOnDeprecatedClasses = noClasses()
      .should()
      .dependOnClassesThat()
      .areAnnotatedWith(Deprecated.class)
      .because("Production code should not depend on deprecated classes");
}