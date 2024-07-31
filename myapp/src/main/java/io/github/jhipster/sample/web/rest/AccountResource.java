package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.config.Constants;
import io.github.jhipster.sample.web.rest.vm.UserVM;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonString;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing the current user's account.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    // Since the resource can be accessed via an HTTP client (ie. service mode), use the Access Token as Bearer token
    @Inject
    JsonWebToken accessToken;

    /**
     * {@code GET /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GET
    @Path("/account")
    @Authenticated
    public UserVM getAccount() {
        if (accessToken == null) {
            throw new AccountResourceException("User could not be found");
        }

        UserVM user = new UserVM();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (accessToken.getClaim("uid") != null) {
            user.id = accessToken.getClaim("uid");
            user.login = accessToken.getClaim("sub");
        } else {
            user.id = accessToken.getClaim("sub");
        }
        if (accessToken.getClaim("preferred_username") != null) {
            user.login = ((String) accessToken.getClaim("preferred_username")).toLowerCase();
        } else if (user.login == null) {
            user.login = user.id;
        }
        if (accessToken.getClaim("given_name") != null) {
            user.firstName = accessToken.getClaim("given_name");
        }
        if (accessToken.getClaim("family_name") != null) {
            user.lastName = accessToken.getClaim("family_name");
        }
        if (accessToken.getClaim("email_verified") != null) {
            user.activated = accessToken.getClaim("email_verified");
        }
        if (accessToken.getClaim("email") != null) {
            user.email = ((String) accessToken.getClaim("email")).toLowerCase();
        } else {
            user.email = accessToken.getClaim("sub");
        }
        if (accessToken.getClaim("langKey") != null) {
            user.langKey = (String) accessToken.getClaim("langKey");
        } else if (accessToken.getClaim("locale") != null) {
            // trim off country code if it exists
            String locale = accessToken.getClaim("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.langKey = locale.toLowerCase();
        } else {
            // set langKey to default if not specified by IdP
            user.langKey = Constants.DEFAULT_LANGUAGE;
        }
        if (accessToken.getClaim("picture") != null) {
            user.imageUrl = accessToken.getClaim("picture");
        }
        user.activated = true;
        user.authorities = getAuthoritiesFromAccessToken();
        return user;
    }

    private Set<String> getAuthoritiesFromAccessToken() {
        return Optional.ofNullable(accessToken.claim(Claims.groups.name()))
            .filter(Set.class::isInstance)
            .map(Set.class::cast)
            .orElseGet(
                () ->
                    accessToken
                        .claim("roles")
                        .filter(JsonArray.class::isInstance)
                        .map(JsonArray.class::cast)
                        .orElse(JsonArray.EMPTY_JSON_ARRAY)
                        .stream()
                        .filter(JsonString.class::isInstance)
                        .map(JsonString.class::cast)
                        .map(JsonString::getString)
                        .filter(role -> role.startsWith("ROLE_"))
                        .collect(Collectors.toSet())
            );
    }
}
