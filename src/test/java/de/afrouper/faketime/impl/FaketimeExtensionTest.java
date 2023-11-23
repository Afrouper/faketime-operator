package de.afrouper.faketime.impl;

import de.afrouper.faketime.handlers.DeploymentEventHandler;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.KubernetesTestServer;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@WithKubernetesTestServer
@QuarkusTest
class FaketimeExtensionTest {

    @KubernetesTestServer
    KubernetesServer mockServer;
    @Inject
    KubernetesClient client;

    @Inject
    DeploymentEventHandler deploymentEventHandler;

    private SharedIndexInformer<Deployment> indexInformer;

    @BeforeEach
    public void setup() {
        indexInformer = client.informers().sharedIndexInformerFor(Deployment.class, 1000);
        indexInformer.addEventHandler(deploymentEventHandler);
        indexInformer.start();
    }

    @Test
    public void deploymentTest() throws Exception {
        //TODO: Build proper deployment
        Deployment deployment = new DeploymentBuilder()
                .editMetadata()
                .withName("Test-Deployment")
                .addToLabels("de.afrouper.useFaketime", "true")
                .endMetadata()
                .editOrNewSpec()
                .editOrNewTemplate()
                .endTemplate()
                .endSpec()
                .build();

        client.resource(deployment).create();

        Thread.sleep(5000);
    }
}