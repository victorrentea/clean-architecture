## Title
Using @Builder pattern with large records

## Status
Proposed

## Context
After migrating to Java 17 and deciding to use Records as DTOs, 
we found it difficult to follow the fields
order/ distinguish between them when 
instantiating a new record with multiple fields.

## Decision
We will annotate any record with >= 5 fields with @Builder from lombok.

## Consequences
#### Pros:
 - more readable
 - fewer chances to introduce bugs by confusing one field with another

#### Cons:
 - more Lombok magic to understand
 - losing consistency regarding the way we're instantiating records
 - ⚠️ careful with Optional fields: should pass Optional.empty()

## Enforcement
By code review. :(

#### Example:
```
public record Employee(
    String firstName,
    String lastName,
    int age,
) {
    public static class Builder {
        private String firstName;
        private String lastName;
        private int age;
        private String department;
        private String email;
        private double salary;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Employee build() {
            return new Employee(firstName, lastName, age, department, email, salary);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
```
#### Usage
```
Employee employee = Employee.builder()
    .firstName("John")
    .lastName("Doe")
    .age(30)
    .department("Engineering")
    .email("john.doe@example.com")
    .salary(75000)
    .build();
```