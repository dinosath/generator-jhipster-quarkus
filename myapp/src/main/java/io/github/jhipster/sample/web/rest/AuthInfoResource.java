package io.github.jhipster.sample.web.rest;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Resource to return information about OIDC properties
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthInfoResource {

    final String issuer;
    final String clientId;

    @Inject
    public AuthInfoResource(
        @ConfigProperty(name = "quarkus.oidc.auth-server-url") String issuer,
        @ConfigProperty(name = "quarkus.oidc.client-id") String clientId
    ) {
        this.issuer = issuer;
        this.clientId = clientId;
    }

    @GET
    @Path("/auth-info")
    @PermitAll
    public AuthInfoVM getAuthInfo() {
        return new AuthInfoVM(issuer, clientId);
    }

    @RegisterForReflection
    public static class AuthInfoVM {

        public final String issuer;
        public final String clientId;

        AuthInfoVM(String issuer, String clientId) {
            this.issuer = issuer;
            this.clientId = clientId;
        }
    }
}
