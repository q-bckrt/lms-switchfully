package switchfully.lms.utility.security;

import switchfully.lms.domain.UserRole;

public record KeycloakUserDTO (String userName, String password, UserRole role){
}
