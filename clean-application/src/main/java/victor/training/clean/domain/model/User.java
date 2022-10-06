package victor.training.clean.domain.model;

import lombok.experimental.Delegate;
import victor.training.clean.infra.LdapUserDto;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class User { //
    // #2 alternative design: HIDE inside the original object in a ...Wrapper and adapt on the fly the data
    // - you coupld your User domain VO to the DTO => you cannot DIP anymore.
//    private LdapUserDto dto;

    // #1 recreate state
    // + cleaner but ugly if many fields
    //  Value Object = immutable (small) object whose identity is computed comparing all fields.
    //      they lack a 'continuity of change'

    private final String username; // not uId
    private final String workEmail;
    private final String corporateName;

    public User(String username, String workEmail, String corporateName) {
        this.username = requireNonNull(username);
        this.workEmail = workEmail;
        this.corporateName = corporateName;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getWorkEmail() {
        return ofNullable(workEmail);
    }



    public String getCorporateName() {
        return corporateName;
    }
}
