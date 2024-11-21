## Title
   Using Aspects instead of Spring Security filters to implement login

## Context
   Our application requires user authentication to secure resources. 
   Both Spring filters and Spring aspects are potential 
   mechanisms for handling login and authentication logic.
   The team previously considered using Spring aspects due
   to their ability to manage cross-cutting concerns like 
   logging and authentication in a decoupled way. 
   
## Decision
   We use aspects instead of Spring Security filters to implement login.

## Consequences
### Pros
+ Harder to add unit tests for aspects than filters.

### Cons
+ Filters provide direct access to raw HTTP requests and responses, enabling comprehensive control over authentication.
+ Maintain a clear separation between web-layer concerns and application-layer responsibilities.
+ Developers can utilize their familiarity with servlet filters, minimizing the learning curve.
+ Avoid possible issues if controller is moved in another package
