//package victor.training.clean;
//
//import com.tngtech.archunit.core.domain.JavaAnnotation;
//import com.tngtech.archunit.core.domain.JavaClass;
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.junit.ArchTest;
//import com.tngtech.archunit.lang.ArchRule;
//import com.tngtech.archunit.lang.ConditionEvents;
//import com.tngtech.archunit.lang.SimpleConditionEvent;
//import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
//import lombok.val;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.stereotype.Controller;
//import org.springframework.test.annotation.Timed;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
//import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
//import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
//
//public class YourArchitectureTest {
//
//  // @pedro ---
//  @ArchTest
//  static final ArchRule HEXAGONAL_ARCHITECTURE = layeredArchitecture()
//      .consideringOnlyDependenciesInLayers()
//      .layer("Api").definedBy("..api..")
//      .layer("Application").definedBy("..application..")
//      .layer("Domain").definedBy("..domain..")
//      .layer("Infrastructure").definedBy("..infrastructure..")
//      .whereLayer("Domain").mayNotAccessAnyLayer() // agnostic
////      .whereLayer("Application").mayOnlyAccessLayers("Domain", "Api", "Infrastructure")
//      .whereLayer("Api").mayOnlyAccessLayers("Domain")
//      .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain");
//
//  @ArchTest
//  static final ArchRule FREE_OF_CYCLES = slices().matching("com.flutter.cebo.(*)..")
//      .should().beFreeOfCycles();
//
//  // -- diogo
//  private static final String CORE_PACKAGE = "com.flutter.boss.core..";
//  private static final String FRAMEWORK_PACKAGE = "org.springframework..";
//  private static final String BASE_PACKAGE = "";
//
//  @Test
//  void coreModuleShouldNotDependOnFrameworks() {
//    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.flutter.boss");
//
//    ArchRule rule = noClasses()
//        .that().resideInAPackage(CORE_PACKAGE)
//        .should().dependOnClassesThat().resideInAPackage(FRAMEWORK_PACKAGE);
//
//    rule.check(importedClasses);
//  }
//
//  //  -- nuno
//  @Test
//  void domainPackageShouldNotDependOnInputOrOutput() {
//    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example");
//
//    ArchRuleDefinition.noClasses()
//        .that().resideInAPackage("..domain..")
//        .should().dependOnClassesThat()
//        .resideInAnyPackage("..input..", "..output..")
//        .check(importedClasses);
//  }
//
//  //--- @filipe
//  @Test
//  void restTemplateClientsShouldBeAnnotatedWithTimedAndHistogramTrue() {
//    JavaClasses importedClasses = new ClassFileImporter()
//        .importPackages("com.flutter.gbp.qos");
//
//    ArchRule rule = ArchRuleDefinition.classes()
//        .that().haveFieldWithType(RestTemplate.class)
//        .should(new com.tngtech.archunit.lang.ArchCondition<JavaClass>(
//            "be annotated with @Timed and histogram=true") {
//          @Override
//          public void check(JavaClass javaClass, ConditionEvents events) {
//            boolean hasTimed = false;
//            boolean histogramTrue = false;
//
//            for (JavaAnnotation annotation : javaClass.getAnnotations()) {
//              if (annotation.getRawType().isEquivalentTo(Timed.class)) {
//                hasTimed = true;
//                Object histogramValue = annotation.get("histogram");
//                if (histogramValue instanceof Boolean && (Boolean) histogramValue) {
//                  histogramTrue = true;
//                }
//              }
//            }
//
//            boolean satisfied = hasTimed && histogramTrue;
//            events.add(new SimpleConditionEvent(
//                javaClass, satisfied,
//                javaClass.getName() + " should be annotated with @Timed and histogram=true"));
//          }
//        });
//
//    rule.check(importedClasses);
//  }
//
//
//  // @joel
//  @Test
//  void commonsModelShouldNotDependOnOtherPackages() {
//    JavaClasses classes = new ClassFileImporter().importPackages("com.fanduel.commons");
//
//    ArchRule rule = noClasses()
//        .that().resideInAPackage("com.fanduel.commons")
//        .should().dependOnClassesThat()
//        .resideOutsideOfPackages(
//            "com.fanduel.commons",
//            "java..",          // allow Java SDK
//            "javax..",         // allow javax if needed
//            "jakarta.."        // allow Jakarta EE if needed
//        );
//
//    rule.check(classes);
//  }
//
//  //--- @bruno
//  @Test
//  void clientsShouldNotDependOnOtherPackages() {
//    JavaClasses importedClasses = new ClassFileImporter()
//        .importPackages("victor.training.clean.domain");
//
//    ArchRuleDefinition.noClasses()
//        .that().resideInAPackage("victor.training.clean.domain")
//        .should().dependOnClassesThat()
//        .resideOutsideOfPackages(
//            "victor.training.clean.domain",
//            "java.."
//        )
//        .check(importedClasses);
//  }
//
//  // -- @joao
//  @Test
//  void controllers_should_only_depend_on_converters_and_services() {
//    JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);
//
//    ArchRule rule = classes()
//        .that()
//        .areMetaAnnotatedWith(Controller.class)
//        .should()
//        .onlyAccessClassesThat(
//            // Allow access to:
//            (JavaClass clazz) -> clazz.getPackageName().startsWith("java.") || // Java stdlib
//                                 clazz.getPackageName().startsWith("org.springframework") || // Spring framework
//                                 clazz.getPackageName().startsWith(BASE_PACKAGE + ".converter") || // Converters
//                                 clazz.getPackageName().startsWith(BASE_PACKAGE + ".service") // Services
//        );
//
//    rule.check(importedClasses);
//  }
//
//  // -- @francisco
//  @Nested
//  static class MessageListenerArchitectureTest {
//
//    private final JavaClasses importedClasses = new ClassFileImporter()
//        .importPackages("com.example"); // Replace with your base package
//
//    @Test
//    void messageListenerImplementationsShouldBeAnnotatedWithTimed() {
//      ArchRule rule = classes()
//          .that().implement("MessageListener") // Replace with your full interface name if needed
//          .should().beAnnotatedWith(Timed.class)
//          .because("All MessageListener implementations must be monitored with @Timed annotation");
//
//      rule.check(importedClasses);
//    }
//
//    @Test
//    void messageListenerImplementationsShouldBeAnnotatedWithTimedAlternative() {
//      // Alternative approach if you want to be more specific about the interface
//      ArchRule rule = classes()
//          .that().areAssignableTo("com.example.messaging.MessageListener") // Use full qualified name
//          .and().areNotInterfaces()
//          .should().beAnnotatedWith("io.micrometer.core.annotation.Timed")
//          .because("All MessageListener implementations must be monitored with @Timed annotation");
//
//      rule.check(importedClasses);
//    }
//
//    @Test
//    void messageListenerImplementationsShouldBeAnnotatedWithTimedUsingName() {
//      // If you want to match by simple class name
//      ArchRule rule = classes()
//          .that().implement("MessageListener")
//          .should().beAnnotatedWith("Timed")
//          .because("All MessageListener implementations must be monitored with @Timed annotation");
//
//      rule.check(importedClasses);
//    }
//  }
//
//  // -- @rafael
////  @Tag("UnitTest")
////  class ArchTest {
////
////    private val importedClasses = ClassFileImporter().importPackages("com.fanduel.sportsbook.rde")
////
////    @Test
////    fun `controllers should not depend on repositories`() {
////      val rule = ArchRuleDefinition.noClasses()
////          .that().resideInAPackage("..controller..")
////          .should().dependOnClassesThat().resideInAPackage("..repository..")
////      rule.check(importedClasses)
////    }
////
////    @Test
////    fun `repositories should not use services`() {
////      val rule = ArchRuleDefinition.noClasses()
////          .that().resideInAPackage("..repository..")
////          .should().dependOnClassesThat().resideInAPackage("..service..")
////      rule.check(importedClasses)
////    }
////
////    @Test
////    fun `domain should not use DTOs`() {
////      val rule = ArchRuleDefinition.noClasses()
////          .that().resideInAPackage("..domain..")
////          .should().dependOnClassesThat().resideInAPackage("..dto..")
////      rule.check(importedClasses)
////    }
////  }
//
//  //-- @alexandre
//  @Nested
//  class ManagerServiceDependencyTest {
//
//    @Test
//    public void manager_classes_should_not_depend_on_service_classes() {
//      ArchRule rule = noClasses()
//          .that().resideInAPackage("..manager..")
//          .should().dependOnClassesThat().resideInAPackage("..service..")
//          .because("Managers should orchestrate business logic through repositories, not services, " +
//                   "to maintain clear layer separation and prevent circular dependencies");
//
//      rule.check(importedClasses);
//    }
//
//    /**
//     * Specific Rule: Manager implementation classes should not depend on service classes.
//     * This is a more specific rule targeting manager implementations specifically.
//     * Current Status: ❌ FAILING – 52 violations detected
//     * Action Required: Refactor #ManagerImpl classes to inject repositories instead of services
//     */
//    @Test
//    public void manager_implementations_should_not_depend_on_service_classes() {
//      ArchRule rule = noClasses()
//          .that().resideInAPackage("..manager..")
//          .and().haveSimpleNameEndingWith("ManagerImpl")
//          .should().dependOnClassesThat().resideInAPackage("..service..")
//          .because("Manager implementations should use repositories and domain objects, not services");
//
//      rule.check(importedClasses);
//    }
//
//    /**
//     * Interface Rule: Manager interfaces should not depend on service classes.
//     * This ensures that even the contracts don't create unwanted dependencies.
//     * Current Status: ✅ PASSING – 0 violations
//     * Maintenance: Keep this clean by not adding service dependencies to manager interfaces
//     */
//    @Test
//    public void manager_interfaces_should_not_depend_on_service_classes() {
//      ArchRule rule = noClasses()
//          .that().resideInAPackage("..manager..")
//          .and().haveSimpleNameEndingWith("Manager")
//          .and().areInterfaces()
//          .should().dependOnClassesThat().resideInAPackage("..service..")
//          .because("Manager contracts should not expose service dependencies in their APIs");
//
//      rule.check(importedClasses);
//    }
//  }
//}
