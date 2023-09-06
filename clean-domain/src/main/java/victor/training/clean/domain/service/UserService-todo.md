1. Use Domain Objects in core logic
   - create a new dedicated data structure and map LdapUserDto to it
   - how to name the new class?
   - in what package to place it? what kind of structure is it: infra, domain, repo, dto ?
   - can you please 🙏 avoid using Lombok for it?
   - would you make it mutable or immutable (no setters)?
   - make its fields as convenient for my complex logic as possible 
   - replace the usage of LdapUserDto with the new object inside the complexLogic()
2. Move integration concerns to an Adapter
   - extract the interaction with LdapApi in a separate class (=Adapter design pattern)
   - make sure the signature of the Adapter's method (name and parameters) is as convenient as possible for the Domain logic
3. Dependency Inversion Principle
   - make sure there is nothing related to LdapApi left in the UserService
   - move the adapter to the 'infra' package
   - extract an interface (for what class?)
   - make sure there is nothing in the 'domain' depending on anything in the 'infra' package (aka **⭐️Agnostic Domain**)
   - this test should pass: victor.training.clean.ArchitectureTest.domain_independent_of_infrastructure