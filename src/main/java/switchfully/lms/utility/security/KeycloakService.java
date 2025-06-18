package switchfully.lms.utility.security;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import switchfully.lms.utility.exception.UserAlreadyExistsException;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

@Service
public class KeycloakService {

    private final RealmResource realmResource;
    private final String clientID;

    public KeycloakService(Keycloak keycloak, @Value("${keycloak.realm}") String realmName, @Value("${keycloak.resource}") String clientId) {
        this.clientID = clientId;
        this.realmResource = keycloak.realm(realmName);
    }

    public void addUser(KeycloakUserDTO keycloakUserDTO) {
        String createdUserId = createUser(keycloakUserDTO);
        getUser(createdUserId).resetPassword(createCredentialRepresentation(keycloakUserDTO.password()));
        addRole(getUser(createdUserId), keycloakUserDTO.role().name());
    }

    public void changePassword(KeycloakUserDTO keycloakUserDTO) {
        UserResource user = getUser(keycloakUserDTO.userName());
        user.resetPassword(createCredentialRepresentation(keycloakUserDTO.password()));
    }

    private Response createUserRealm(KeycloakUserDTO keycloakUserDTO) {
        return realmResource.users().create(createUserRepresentation(keycloakUserDTO));
    }

    private String createUser(KeycloakUserDTO keycloakUserDTO) {
        try {
            return CreatedResponseUtil.getCreatedId(createUserRealm(keycloakUserDTO));
        } catch (WebApplicationException exception) {
            throw new UserAlreadyExistsException(keycloakUserDTO.userName());
        }
    }

    private CredentialRepresentation createCredentialRepresentation(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void addRole(UserResource user, String roleName) {
        user.roles().clientLevel(getClientUUID()).add(List.of(getRole(roleName)));
    }

    private String getClientUUID() {
        return realmResource.clients().findByClientId(clientID).get(0).getId();
    }


    private UserResource getUser(String username) {
        List<UserRepresentation> users = realmResource.users().search(username, true);

        if (users.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }

        String userId = users.getFirst().getId();
        return realmResource.users().get(userId);
    }

    private RoleRepresentation getRole(String roleToAdd) {
        return getClientResource().roles().get(roleToAdd).toRepresentation();
    }

    private ClientResource getClientResource() {
        return realmResource.clients().get(getClientUUID());
    }

    private UserRepresentation createUserRepresentation(KeycloakUserDTO keycloakUserDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(keycloakUserDTO.userName());
        user.setEnabled(true);
        user.setFirstName(keycloakUserDTO.firstName());
        user.setLastName(keycloakUserDTO.lastName());
        user.setEmail(keycloakUserDTO.email());
        user.setEmailVerified(true);
        return user;
    }
}