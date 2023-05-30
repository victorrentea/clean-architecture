package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import com.tngtech.archunit.library.dependencies.syntax.GivenSlices;
import org.junit.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.assertj.core.api.Assertions.assertThat;

public class IndependentSubdomainsArchUnitTest {

   private static final String[] ALLOWED_SHARED_PACKAGES = {"..common..", "..api.."};

   private JavaClasses classes = new ClassFileImporter().importPackages("victor");
   private GivenSlices slices = SlicesRuleDefinition.slices()
           .matching("..clean.(*)..*");

   @Test
   public void independentSubdomains() {
      SliceRule sliceRule = slices
          .should().notDependOnEachOther()
          .ignoreDependency(alwaysTrue(), resideInAnyPackage(ALLOWED_SHARED_PACKAGES));

      // progressive strangling the monolith
      List<String> violations = sliceRule.evaluate(classes).getFailureReport().getDetails();

      // A: decoupling phase: progressively lower this number:
      assertThat(violations).hasSizeLessThanOrEqualTo(123); // <-- real life: starting point after moving classes around
      assertThat(violations).hasSizeLessThanOrEqualTo(0); // goal, 6 mo from now

      // B: maintenance phase: fail test at any deviation
      // sliceRule.check(classes);
   }

   @Test
   public void noCyclesBetweenSlices() {
      slices.should().beFreeOfCycles().check(classes);
   }
}
