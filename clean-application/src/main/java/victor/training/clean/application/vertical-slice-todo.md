## Group code by endpoint, not by technical layers
1. GetCustomerByIdUseCase
   - create this class with a single endpoint inside (the public method)
   - bring all the code associated with this endpoint in this class 
   - move the Dto inside as static nested class, rename it to `...Response`
   - use `@VisibleForTesting` on the DTO to stop other slices from using the same object
   - remove the corresponding code from the CustomerApplicationService - the LargeIntegrationTest should still pass.
   - [optional, at end] use a MapStruct mapper interface inside this class to make it work.
2. SearchCustomerUseCase
   - same as above - all the code of this use-case should be inside the class
   - Hint: use 'victor.training.clean.application.SearchCustomerUseCase$SearchCustomerResponse' as the Dto name in JPQL
3. RegisterCustomerUseCase
   - same as above
   - would you keep the Domain Service? (! if it's not reused)

