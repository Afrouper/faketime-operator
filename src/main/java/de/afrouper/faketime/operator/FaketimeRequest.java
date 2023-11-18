package de.afrouper.faketime.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("faketime.io")
@Version("v1alpha1")
@ShortNames("ft")
public class FaketimeRequest extends CustomResource<FaketimeSpec, FaketimeStatus> implements Namespaced {
}
