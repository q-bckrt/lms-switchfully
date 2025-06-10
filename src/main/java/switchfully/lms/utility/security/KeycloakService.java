package switchfully.lms.utility.security;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
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

    private final RealmResource realmRessource;
    private final String clientID;

    public KeycloakService(Keycloak keycloak, @Value("${keycloak.realm}") String realmName, @Value("${keycloak.resource}") String clientId) {
        this.clientID = clientId;
        this.realmRessource = keycloak.realm(realmName);
    }

    // create user in the realm
    private Response createUser(String username) {
        return realmRessource.users().create(createUserRepresentation(username));
    }

    // create a representation ?
    private String createUser(KeycloakUserDTO keycloakUserDTO) {
        try {
            return CreatedResponseUtil.getCreatedId(createUser(keycloakUserDTO.userName()));
        } catch (WebApplicationException exception) {
            throw new UserAlreadyExistsException(keycloakUserDTO.userName());
        }
    }


    public void addUser(KeycloakUserDTO keycloakUserDTO) {
        String createdUserId = createUser(keycloakUserDTO);
        getUser(createdUserId).resetPassword(createCredentialRepresentation(keycloakUserDTO.password()));
        addRole(getUser(createdUserId), keycloakUserDTO.role().name());
    }
    private UserResource getUser(String userId) {
        return realmRessource.users().get(userId);
    }


    private ClientResource getClientResource() {
        return realmRessource.clients().get(getClientUUID());
    }

    private String getClientUUID() {
        return realmRessource.clients().findByClientId(clientID).get(0).getId();
    }

    private UserRepresentation createUserRepresentation(String username) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        return user;
    }



    private void addRole(UserResource user, String roleName) {
        user.roles().clientLevel(getClientUUID()).add(List.of(getRole(roleName)));
    }

    private RoleRepresentation getRole(String roleToAdd) {
        return getClientResource().roles().get(roleToAdd).toRepresentation();
    }



    private void changePassword(KeycloakUserDTO keycloakUserDTO) {
        UserResource user = getUser(keycloakUserDTO.userName());
        user.resetPassword(createCredentialRepresentation(keycloakUserDTO.password()));
    }
    private CredentialRepresentation createCredentialRepresentation(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


}
