package io.github.jhipster.sample.web.rest;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to authenticate users.
 */
@Path("/")
@RequestScoped
public class UserOauth2Controller {

    private final Logger log = LoggerFactory.getLogger(UserOauth2Controller.class);

    @GET
    @Path("oauth2/authorization/oidc")
    @Authenticated
    public Response initCodeFlow(@Context UriInfo uriInfo) {
        return Response.status(Response.Status.FOUND).location(uriInfo.getBaseUri()).build();
    }

    @GET
    @Path("login/oauth2/code/oidc")
    @Authenticated
    public Response callback(@Context UriInfo uriInfo) {
        return Response.status(Response.Status.FOUND).location(uriInfo.getBaseUri()).build();
    }
}
