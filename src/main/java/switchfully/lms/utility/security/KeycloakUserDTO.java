package switchfully.lms.utility.security;

import switchfully.lms.domain.UserRole;

public record KeycloakUserDTO (String userName, String firstName, String lastName,String password, UserRole role, String email){
}
