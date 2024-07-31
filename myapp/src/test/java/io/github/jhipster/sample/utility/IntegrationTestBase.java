package io.github.jhipster.sample.utility;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.http.ContentType;
import java.util.Map;

/**
 * Base class encapsulating login/logout functions required by all Integration
 * tests. RestAssured context is derived from <code>@TestHTTPEndpoint</code>
 * annotation defined in the derived class.
 */
public abstract class IntegrationTestBase {

    protected String getKeycloakServerUrl() {
        return "http://localhost:9080";
    }

    protected String getKeycloakClientId() {
        return "web_app";
    }

    protected String getKeycloakClientSecret() {
        return "web_app";
    }

    protected String getAccessToken(String username, String password) {
        return given()
            .contentType(ContentType.URLENC)
            .formParams(
                Map.of(
                    "username",
                    username,
                    "password",
                    password,
                    "grant_type",
                    "password",
                    "client_id",
                    getKeycloakClientId(),
                    "client_secret",
                    getKeycloakClientSecret()
                )
            )
            .when()
            .post(getKeycloakServerUrl() + "/protocol/openid-connect/token")
            .then()
            .statusCode(OK.getStatusCode())
            .body("access_token", instanceOf(String.class))
            .body("access_token", notNullValue())
            .extract()
            .path("access_token");
    }
}
