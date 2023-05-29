1. Implement Exception Handling with a Global Exception Handler
   - Remove the 2 x `catch` returning 404 Not Found for NoSuchElementException from the `CustomerController`
   - Add a new method to the `@RestControllerAdvice`-annotated class:
     ```
     @ResponseStatus(NOT_FOUND)
     @ExceptionHandler(NoSuchElementException.class)
     public String handleGeneralException() {
       return "Not found";
     }
     ```
   - Run the `LargeIntegrationTest` to make sure it still works.
2. Collapse the anemic controller layer
   - Move the following annotations from Controller to the `CustomerApplicationService`:
     - @GetMapping, @PostMapping, @PutMapping, @RequestBody, @PathVariable, @Validated
     - @Operation
     - @RestController
   - Delete the controller
   - Run the `LargeIntegrationTest` to make sure it still works.
   - [optional] To remove annotations from your ApplicationService:
     - Extract an interface from it (Refactor>Extract Interface)
     - Remove the annotations from the ApplicationService (they are 'inherited' from the implemented interface)
3. De-clutter `CustomerApplicationService.findById` from
   - The bit of reusable business logic (**it repeats!**)
   - The boring mapping code
4. Pragmatic Validation: customer.name.length has to have minimum 5 characters 
   - Use annotation @Size(min = 5) instead of the `if` (remove it)
   - Check the `LargeIntegrationTest`
   - Set a custom message for the validation failure + check it in the LargeIntegrationTest
     - Hint: add to annotation `, message = "{customer-name-too-short}"` to use a message defined in src/main/resources/messages.properties
     - Check the error message in the test
5. Shrink the `CustomerApplicationService.register`
   - Push Mapping logic somewhere else
   - Push Domain Logic to lower-level Domain Services in victor.training.clean.domain.service
   - Make sure that the test passes: `victor.training.clean.ArchitectureTest#domain_independent_of_application`
