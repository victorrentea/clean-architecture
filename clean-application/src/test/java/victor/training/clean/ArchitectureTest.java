package victor.training.clean;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchitectureTest {

   private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("victor.training");

   @Test
   // NOTE: In case you don't understand this test, contact me:
   // +40720019564 or victorrentea@gmail.com (the anarchitect)
   public void domain_independent_of_infrastructure() {
      noClasses().that().resideInAPackage("..domain..")
          .should().dependOnClassesThat().resideInAPackage("..infra..")
          .check(allProjectClasses);
   }

   @Test
   public void service_independent_of_facade() {
      noClasses().that().resideInAPackage("..service..")
          .should().dependOnClassesThat().resideInAPackage("..facade..")
          .check(allProjectClasses);
   }
//   @Test
//   public void service_independent_of_facade() {
//      noClasses().that().resideInAPackage("..service..")
//          .should().dependOnClassesThat().resideInAPackage("..repo..")
//          .check(allProjectClasses);
//   }

   @Test
//   @Disabled
   public void domain_not_exposed_via_controller_methods() {
      methods().that().areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .and().arePublic()
          .should().notHaveRawReturnType(JavaClass.Predicates.resideInAnyPackage("..domain.."))
          .andShould(new NotReturnGenericFrom(".domain."))

          .check(allProjectClasses);
   }

    private static class NotReturnGenericFrom extends ArchCondition<JavaMethod> {
        private final String illegalPackage;

        public NotReturnGenericFrom(String illegalPackage) {
            super("not return generic from package " + illegalPackage);
            this.illegalPackage = illegalPackage;
        }

        @Override
        public void check(JavaMethod javaMethod, ConditionEvents conditionEvents) {
            JavaType returnType = javaMethod.getReturnType();
            if (returnType instanceof JavaParameterizedType) {
                JavaParameterizedType pt = (JavaParameterizedType) returnType;
                System.out.println("java m: " + javaMethod + " with " + pt.getActualTypeArguments());
                boolean ok = pt.getActualTypeArguments().stream().noneMatch(v -> v.getName().contains(illegalPackage));
                conditionEvents.add(new SimpleConditionEvent(javaMethod, ok, "RestController method leaking domain"));
            }

        }
    }
}
