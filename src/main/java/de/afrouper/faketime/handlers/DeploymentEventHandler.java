package de.afrouper.faketime.handlers;

import de.afrouper.faketime.impl.FaketimeExtension;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DeploymentEventHandler implements ResourceEventHandler<Deployment> {

    private final FaketimeExtension faketimeExtension;

    @Inject
    public DeploymentEventHandler(FaketimeExtension faketimeExtension) {
        this.faketimeExtension = faketimeExtension;
    }

    @Override
    public void onAdd(Deployment deployment) {
        faketimeExtension.add(deployment);
    }

    @Override
    public void onUpdate(Deployment oldDeployment, Deployment newDeployment) {

    }

    @Override
    public void onDelete(Deployment deployment, boolean deletedFinalStateUnknown) {

    }
}
