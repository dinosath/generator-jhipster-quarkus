package io.github.jhipster.sample.web.rest;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.jhipster.sample.TestUtil;
import io.github.jhipster.sample.infrastructure.MockOidcServerTestResource;
import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class LogoutResourceTest {

    public static final String ORIGIN_HEADER = "http://localhost:9999";

    @InjectMock
    @IdToken
    JsonWebToken idToken;

    @InjectMock
    OidcConfigurationMetadata configMetadata;

    @Test
    public void getLogoutInformation() {
        var logoutEndpoint = "dummy-logout-url";
        var rawIdToken = "1234";
        when(idToken.getRawToken()).thenReturn(rawIdToken);
        when(configMetadata.getIssuer()).thenReturn("dummy-issuer-url");
        when(configMetadata.getEndSessionUri()).thenReturn(logoutEndpoint);

        var jsonPath = given()
            .auth()
            .preemptive()
            .oauth2(TestUtil.getAdminToken())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .header("origin", ORIGIN_HEADER)
            .when()
            .post("/api/logout")
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .jsonPath();

        assertThat(jsonPath.getString("logoutUrl")).isEqualTo(
            logoutEndpoint + "?id_token_hint=" + rawIdToken + "&post_logout_redirect_uri=" + ORIGIN_HEADER
        );
    }

    @Test
    public void getAuth0LogoutInformation() {
        var issuerUrl = "dummy-auth0.com";
        when(configMetadata.getIssuer()).thenReturn(issuerUrl);

        var jsonPath = given()
            .auth()
            .preemptive()
            .oauth2(TestUtil.getAdminToken())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .header("origin", ORIGIN_HEADER)
            .when()
            .post("/api/logout")
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .jsonPath();

        assertThat(jsonPath.getString("logoutUrl")).isEqualTo(issuerUrl + "/v2/logout?client_id=dummy&returnTo=" + ORIGIN_HEADER);
    }
}
