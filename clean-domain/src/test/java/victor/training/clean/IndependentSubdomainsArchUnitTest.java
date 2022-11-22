package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.assertj.core.api.Assertions.assertThat;

public class IndependentSubdomainsArchUnitTest {

   @Test
   public void independentSubdomains() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor");

      SliceRule sliceRule = SlicesRuleDefinition.slices().matching("..clean.(*)..*")
          .should().notDependOnEachOther()
          .ignoreDependency(alwaysTrue(), resideInAnyPackage(new String[]{"..shared..", "..door.."})); // allow dependencies to .events

      // progressive strangling the monolith
      List<String> violations = sliceRule.evaluate(classes).getFailureReport().getDetails();

      // A: decoupling phase: progressively lower this number:
//      assertThat(violations).hasSizeLessThanOrEqualTo(2); // <-- real life: starting point after moving classes around
      assertThat(violations).hasSizeLessThanOrEqualTo(0); // goal, 6 mo from now

      // B: maintenance phase: fail test at any deviation
      // sliceRule.check(classes);
   }
}
