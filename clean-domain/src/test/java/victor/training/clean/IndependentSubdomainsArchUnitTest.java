package victor.training.ddd;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.Test;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

public class IndependentSubdomainsArchUnitTest {

   @Test
   public void independentSubdomains() {
      JavaClasses classes = new ClassFileImporter()
          .importPackages("victor.training.ddd");

      String names = classes.stream().map(JavaClass::getSimpleName).collect(joining());
      System.out.println("Studying classes: " + names);

      SliceRule sliceRule = SlicesRuleDefinition.slices()
          .matching("..ddd.(*).*")
          // example: service.order should not depend on service.customer
          .should().notDependOnEachOther()
          .ignoreDependency(alwaysTrue(), resideInAnyPackage("..common..", "..infra")); // allow dependencies to .events

      // progressive strangling the monolith
      EvaluationResult evaluationResult = sliceRule.evaluate(classes);
      int violations = evaluationResult.getFailureReport().getDetails().size();
      System.out.println("Number of violations: " + violations);

      assertThat(violations).isLessThan(110);

      sliceRule.check(classes);


   }
}
