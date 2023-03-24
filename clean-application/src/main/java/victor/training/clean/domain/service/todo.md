1. Use Domain Objects in core logic
   - create a new dedicated data structure and map LdapUserDto to it
   - how to name the new class?
   - in what package to place it? what kind of structure is it: infra, domain, repo, dto ?
   - make its fields as convenient for my complex logic as possible 
   - replace the usage of LdapUserDto with the new object inside the complexLogic()
2. Adapter Design Pattern
   - extract the interaction with LdapApi in a separate class
   - make sure the signature of the Adapter is as convenient as possible for the Domain logic
3. Dependency Inversion Principle
   - make sure there is nothing related to LdapApi left in the UserService
   - move the adapter to the 'infra' package
   - make sure the 'domain' package has nothing to do with the 'infra' package
   - this test should pass: victor.training.clean.ArchitectureTest.domain_independent_of_infrastructure