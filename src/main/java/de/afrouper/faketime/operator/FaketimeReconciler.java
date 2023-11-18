package de.afrouper.faketime.operator;

import io.javaoperatorsdk.operator.api.reconciler.*;

@ControllerConfiguration(namespaces = Constants.WATCH_ALL_NAMESPACES)
public class FaketimeReconciler implements Reconciler<FaketimeRequest> {
    @Override
    public UpdateControl<FaketimeRequest> reconcile(FaketimeRequest faketimeRequest, Context<FaketimeRequest> context) throws Exception {
        return UpdateControl.noUpdate();
    }
}
