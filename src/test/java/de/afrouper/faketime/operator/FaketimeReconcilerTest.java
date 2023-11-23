package de.afrouper.faketime.operator;

import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.KubernetesTestServer;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@WithKubernetesTestServer
class FaketimeReconcilerTest {

    @KubernetesTestServer
    KubernetesServer mockServer;

    @Test
    public void reconcile() throws Exception{

    }
}