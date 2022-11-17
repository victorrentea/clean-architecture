package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.assertj.core.api.Assertions.assertThat;

public class IndependentSubdomainsArchUnitTest {

   private static final String[] ALLOWED_SHARED_PACKAGES = {"..common..", "..api.."};
   @Test
   public void independentSubdomains() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor");

      SliceRule sliceRule = slices().matching("..clean.(*)..*")
          .should().notDependOnEachOther()
          .ignoreDependency(alwaysTrue(), resideInAnyPackage(ALLOWED_SHARED_PACKAGES)); // allow dependencies to .events

      // progressive strangling the monolith
      List<String> violations = sliceRule.evaluate(classes).getFailureReport().getDetails();

      // A: decoupling phase: progressively lower this number:
      assertThat(violations).hasSizeLessThanOrEqualTo(5);

      // B: maintenance phase: fail test at any deviation
       sliceRule.check(classes);
   }§
}
