package io.github.jhipster.sample.infrastructure;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import java.util.Optional;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

/**
 * Keycloak OIDC server resource integration with Quarkus DevServices to
 * authenticate users during Native/Integration tests execution.
 */
public class KeycloakServerResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    private Optional<String> containerNetworkId;
    private KeycloakContainer container;
    private String keycloakServerUrl;

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        containerNetworkId = context.containerNetworkId();
    }

    @Override
    public Map<String, String> start() {
        container = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4").withRealmImportFile("jhipster-realm.json");

        containerNetworkId.ifPresent(container::withNetworkMode);
        container.start();

        keycloakServerUrl = getAuthServerUrl();
        return ImmutableMap.of("quarkus.oidc.auth-server-url", keycloakServerUrl);
    }

    private String getAuthServerUrl() {
        String authServerUrl = container.getAuthServerUrl() + "/realms/jhipster";
        if (containerNetworkId.isPresent()) {
            authServerUrl = "http://" +
            container.getCurrentContainerInfo().getConfig().getHostName() +
            authServerUrl.substring(authServerUrl.lastIndexOf(":"));
        }
        return authServerUrl;
    }

    @Override
    public void stop() {
        if (container != null) {
            container.stop();
            container = null;
        }
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(
            keycloakServerUrl,
            new TestInjector.AnnotatedAndMatchesType(InjectKeycloakServer.class, String.class)
        );
    }
}
