package victor.training.clean;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.Test;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.assertj.core.api.Assertions.assertThat;

public class IndependentSubdomainsArchUnitTest {

   @Test
   public void independentSubdomains() {
      JavaClasses classes = new ClassFileImporter()
          .importPackages("victor.training.clean");


      SliceRule sliceRule = SlicesRuleDefinition.slices()
          .matching("..clean.(*).*")
          // example: service.order should not depend on service.customer
          .should().notDependOnEachOther()
          .ignoreDependency(alwaysTrue(), resideInAnyPackage("..common..", "..infra")); // allow dependencies to .events

      // progressive strangling the monolith
      EvaluationResult evaluationResult = sliceRule.evaluate(classes);
      int violations = evaluationResult.getFailureReport().getDetails().size();
      System.out.println("Number of violations: " + violations);

      assertThat(violations)
          .as("In case this test fails and you don't understand why, " +
              "please contact victorrentea@gmail.com for clarifications")
          .isLessThan(110);

      sliceRule.check(classes);


   }
}
