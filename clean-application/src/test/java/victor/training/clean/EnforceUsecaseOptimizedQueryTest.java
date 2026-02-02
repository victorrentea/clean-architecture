//package victor.training.clean;
//
//import com.tngtech.archunit.core.domain.JavaMethod;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.junit.AnalyzeClasses;
//import com.tngtech.archunit.lang.ArchCondition;
//import com.tngtech.archunit.lang.ArchRule;
//import com.tngtech.archunit.lang.ConditionEvents;
//import com.tngtech.archunit.lang.SimpleConditionEvent;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.jpa.repository.Query;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
//
//@AnalyzeClasses(packages = "victor.training.clean")
//public class EnforceUsecaseOptimizedQueryTest {
//
//  @Test
//  void repository_search_methods_should_not_return_domain_entities() {
//    var imported = new ClassFileImporter().importPackages("victor.training.clean");
//
//    ArchCondition<JavaMethod> notReturnDomainEntity = new ArchCondition<>("not return domain entity types") {
//      @Override
//      public void check(JavaMethod method, ConditionEvents events) {
//        // We'll consider a domain model a class annotated with @Entity (jakarta or javax)
//
//        // Try to inspect via reflection for both raw return type and generic args
//        Method reflected = null;
//        try {
//          reflected = method.reflect();
//        } catch (Exception ignored) {
//        }
//
//        if (reflected != null) {
//          Class<?> raw = reflected.getReturnType(); // erasure
//          if (isEntityClass(raw)) {
//            events.add(SimpleConditionEvent.violated(method,
//                method.getFullName() + " returns domain entity type " + raw.getName()));
//            return;
//          }
//
//          Type generic = reflected.getGenericReturnType();
//          if (generic instanceof ParameterizedType) {
//            for (Type t : ((ParameterizedType) generic).getActualTypeArguments()) {
//              if (t instanceof Class<?> argClass) {
//                if (isEntityClass(argClass)) {
//                  events.add(SimpleConditionEvent.violated(method,
//                      method.getFullName() + " returns collection/optional of domain entity " + argClass.getName()));
//                }
//              } else {
//                // fallback: inspect type name for domain package fragment
//                String typeName = t.getTypeName();
//                if (typeName.contains(".domain.model.")) {
//                  events.add(SimpleConditionEvent.violated(method,
//                      method.getFullName() + " returns collection/optional of domain entity type " + typeName));
//                }
//              }
//            }
//          }
//
//          return; // finished checks using reflection
//        }
//
//        // Fallback when reflection not possible: check raw return JavaClass package name
//        var rawReturn = method.getRawReturnType();
//        if (rawReturn.getPackageName() != null && rawReturn.getPackageName().contains(".domain.model")) {
//          events.add(SimpleConditionEvent.violated(method,
//              method.getFullName() + " returns domain entity type " + rawReturn.getName()));
//        }
//      }
//
//      private boolean isEntityClass(Class<?> cls) {
//        if (cls == null) return false;
//        for (Annotation a : cls.getAnnotations()) {
//          String name = a.annotationType().getName();
//          if ("jakarta.persistence.Entity".equals(name) || "javax.persistence.Entity".equals(name)) {
//            return true;
//          }
//        }
//        return false;
//      }
//    };
//
//    ArchRule rule = methods()
//        .that().areDeclaredInClassesThat().resideInAPackage("..domain.repo..")
//        .and().haveNameMatching("(?i).*search.*")
//        .or().that().areDeclaredInClassesThat().resideInAPackage("..domain.repo..")
//        .and().areAnnotatedWith(Query.class)
//        .should(notReturnDomainEntity);
//
//    rule.check(imported);
//  }
//}
