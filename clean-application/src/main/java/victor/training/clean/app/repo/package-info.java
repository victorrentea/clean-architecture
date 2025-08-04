@NonNullApi
// Tells Spring to throw exception instead of returning a null
//  from any generated finder/@Query methods of any Spring Data Repo in this package
// ==> You'll have to return Optional<> whenever no result might come back.
package victor.training.clean.app.repo;

import org.springframework.lang.NonNullApi;