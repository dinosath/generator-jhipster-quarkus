package io.github.jhipster.sample.web.rest;

import static java.util.Arrays.asList;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.oidc.runtime.DefaultTenantConfigResolver;
import io.quarkus.security.Authenticated;
import io.vertx.core.http.HttpHeaders;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * REST controller for managing global OIDC logout.
 */
@Path("/api/logout")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class LogoutResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @ConfigProperty(name = "quarkus.oidc.authentication.cookie-path")
    String cookiePath;

    @Inject
    OidcConfigurationMetadata configMetadata;

    @Inject
    DefaultTenantConfigResolver tenantResolver;

    /**
     * {@code POST  /api/logout} : logout the current user.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and a body with a global logout URL and ID token.
     */
    @POST
    @Authenticated
    public Response logout() {
        request.getSession().invalidate();
        var cookies = request.getCookies();
        if (cookies != null) asList(cookies).forEach(cookie -> {
            cookie.setValue("");
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
        var logoutUrl = new StringBuilder();
        String authServerUrl = configMetadata.getIssuer();

        if (authServerUrl.contains("auth0.com")) {
            logoutUrl.append(authServerUrl.endsWith("/") ? authServerUrl + "v2/logout" : authServerUrl + "/v2/logout");
        } else {
            logoutUrl.append(configMetadata.getEndSessionUri());
        }
        String originUrl = request.getHeader(HttpHeaders.ORIGIN.toString());

        if (authServerUrl.contains("auth0.com")) {
            logoutUrl
                .append("?client_id=")
                .append(tenantResolver.getTenantConfigBean().getDefaultTenant().getOidcTenantConfig().getClientId().get())
                .append("&returnTo=")
                .append(originUrl);
        } else {
            logoutUrl.append("?id_token_hint=").append(idToken.getRawToken()).append("&post_logout_redirect_uri=").append(originUrl);
        }

        var logoutDetails = Map.of("logoutUrl", logoutUrl.toString());
        return Response.ok(logoutDetails).build();
    }
}
