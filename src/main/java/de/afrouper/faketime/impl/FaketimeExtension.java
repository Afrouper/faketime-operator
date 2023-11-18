package de.afrouper.faketime.impl;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FaketimeExtension {

    @ConfigProperty(name = "faketime.libraryPath")
    String libraryPath;

    @ConfigProperty(name = "faketime.mountPath")
    String mountPath;

    public void add(Deployment deployment) {
        if(Boolean.parseBoolean(deployment.getMetadata().getLabels().getOrDefault("de.afrouper.useFaketime", "false"))) {
            handle(deployment);
        }
        else {
            Log.debugf("Dropping Deployment {}. Label not set", deployment.getMetadata().getName());
        }
    }

    private void handle(Deployment deployment) {
        deployment.getSpec().getTemplate().getSpec().getContainers().forEach(this::handleContainer);
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
