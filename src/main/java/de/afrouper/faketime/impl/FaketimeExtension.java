package de.afrouper.faketime.impl;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceableResource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FaketimeExtension {

    @ConfigProperty(name = "faketime.libraryPath")
    String libraryPath;

    @ConfigProperty(name = "faketime.mountPath")
    String mountPath;

    @Inject
    KubernetesClient kubernetesClient;

    @Inject
    FaketimeConfigMap faketimeConfigMap;

    public void add(Deployment deployment) {
        if(Boolean.parseBoolean(deployment.getMetadata().getLabels().getOrDefault("de.afrouper.useFaketime", "false"))) {
            handle(deployment);
        }
        else {
            Log.debugf("Dropping Deployment {}. Label not set", deployment.getMetadata().getName());
        }
    }

    private void handle(Deployment deployment) {
        checkConfigMap(deployment.getMetadata().getNamespace());

        Volume libfaketime = new VolumeBuilder()
                .withName("libfaketime")
                .withConfigMap(new ConfigMapVolumeSourceBuilder()
                        .withName("libfaketime")
                        .build())
                .build();
        deployment.getSpec().getTemplate().getSpec().getVolumes().add(libfaketime);

        deployment.getSpec().getTemplate().getSpec().getContainers().forEach(this::handleContainer);

    }

    private void checkConfigMap(String namespace) {
        NamespaceableResource<ConfigMap> resource = kubernetesClient.resource(faketimeConfigMap.createConfigMap(namespace));
        resource.create();
    }

    private void handleContainer(Container container) {
        container.getEnv().add(createPreloadVariable());
        VolumeMount libfaketime = new VolumeMountBuilder().withName("libfaketime").withMountPath(mountPath).build();
        container.getVolumeMounts().add(libfaketime);
    }

    private EnvVar createPreloadVariable() {
        EnvVar envVar = new EnvVar();
        envVar.setName("LD_PRELOAD");
        envVar.setValue(libraryPath);
        return envVar;
    }
}
