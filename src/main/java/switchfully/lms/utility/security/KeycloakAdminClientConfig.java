package switchfully.lms.utility.security;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminClientConfig {

    @Bean
    //OLD CONFIG WITH ADMIN USER WE MADE IN KEYCLOAK
//    public Keycloak keycloak(@Value("${keycloak.server-url}") String serverUrl,
//                             @Value("${keycloak.realm}") String realm,
//                             @Value("${keycloak.username}") String username,
//                             @Value("${keycloak.password}") String password,
//                             @Value("${keycloak.client-id}") String clientId) {

    //LOGIN WE USE ON WEBSITE (JAVA USERNAME)
    public Keycloak keycloak(@Value("${keycloak.server-url}") String serverUrl,
                             @Value("${keycloak.realm}") String realm,
                             @Value("${keycloak.username}") String username,
                             @Value("${keycloak.password}") String password,
                             @Value("${keycloak.client-id}") String clientId) {


        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .grantType("password")
                .build();
    }
}