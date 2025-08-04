# ArchUnit Assignment

### Add dependencies to your project

```xml
<dependency>
    <!-- ArchUnit -->
    <groupId>com.tngtech.archunit</groupId>
    <artifactId>archunit-junit5</artifactId>
    <version>1.3.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <!-- AssertJ lib containing assertThat (brought automatically by spring-test) -->
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>
```

### Example Rules

- Repository should not depend on Service or Controller (=cycle)
- Controller should not depend on Repository
- Domain Entity should not depend on DTOs
- DTOs should not have fields of type Domain Entity
- All classes in Domain (Service & Entity) should have <= 300 lines
- Controller should not return Domain Entity
- ...

### References / Examples

- User Guide: https://www.archunit.org/userguide/html/000_Index.html
- https://www.baeldung.com/java-archunit-intro
- https://www.archunit.org/getting-started
- https://github.com/enofex/taikai
- https://github.com/rweisleder/archunit-spring
- https://reflectoring.io/enforce-architecture-with-arch-unit
- https://github.com/xmolecules/jmolecules
- Example: ArchitectureTest.java in this repo
