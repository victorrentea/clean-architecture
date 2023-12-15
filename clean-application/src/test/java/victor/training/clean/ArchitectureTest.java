package victor.training.clean;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import victor.training.clean.utils.ParameterizedReturnTypeCondition;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchitectureTest {

    private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("victor.training");

    @Test //ArchUnit : testing architectural boudnaries on CI!!!!!!!
//    @Disabled
    // NOTE: In case you don't understand this test, contact me:
    // +40720019564 or victorrentea@gmail.com (the anarchitect)
    public void domain_independent_of_infrastructure() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..infra..")
                .check(allProjectClasses);
    }

    // write an archunit rule that stops application package from depenmding on infra package
//    @Test
//public void application_independent_of_infrastructure() {
//        noClasses().that().resideInAPackage("..application..")
//                .should().dependOnClassesThat().resideInAPackage("..infra..")
//                .check(allProjectClasses);
//    }

    @Test
    public void domain_independent_of_application() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..application..")
                .check(allProjectClasses);
    }

    @Test
    @Disabled
    public void domain_not_leaked_via_controller_methods() {
        methods().that().areMetaAnnotatedWith(RequestMapping.class)
                .and().arePublic()
                .should().haveRawReturnType(not(resideInAPackage("..domain..")))
                .andShould(new ParameterizedReturnTypeCondition(not(resideInAPackage("..domain.."))))
                .check(allProjectClasses);
    }

}
