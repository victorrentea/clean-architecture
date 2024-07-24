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

//    @Disabled
    // NOTE: In case you don't understand this test, contact me:
    // call:0800ARCHITECT or victorrentea@gmail.com (the anarchitect)
    // I'm here to help you understand the architecture of your application
    // ⚠️ YOU'LL GET FIRED if you disable it! ⚠️
    // #asktheelders / #askAI
    @Test
    public void domain_independent_of_infrastructure() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                        .resideInAPackage("..infra..")
                .check(allProjectClasses);
    }

    @Test
    public void domain_independent_of_application() {
        // TODO check that no classes in the domain pacakge depend on any classes in the application (eg DTOs)
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
