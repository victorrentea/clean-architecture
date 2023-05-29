## Installation

### Maven install to generate sources 
1. From IntelliJ > View > Tool Windows > Maven > Lifecycle > install
2. From command line: `mvn install`

## Fancy a newer Java version?
If you want to play with `record`s, `switch` expressions and text blocks `"""`, change the <java.version> in parent pom.xml

### Add generated sources in IntelliJ
On the following folder: right click > Mark as > Generated Sources Root
- `clean-application/target/generated-sources/annotations` for MapStruct (CustomerMapStructImpl)
- `clean-application/target/generated-sources/openapi/src/main/java` for OpenAPI (LdapUserDto,...)

