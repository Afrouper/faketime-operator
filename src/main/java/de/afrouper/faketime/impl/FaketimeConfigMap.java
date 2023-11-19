package de.afrouper.faketime.impl;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Singleton
public class FaketimeConfigMap {

    private final String mountPath;
    private final String faketime;
    private final String libfaketime;
    private final String libfaketimeMT;

    public FaketimeConfigMap(@ConfigProperty(name = "faketime.mountPath") String mountPath){
        this.mountPath = mountPath;
        faketime = read("bin/faketime");
        libfaketime = read("lib/faketime/libfaketime.so.1");
        libfaketimeMT = read("lib/faketime/libfaketimeMT.so.1");
    }

    public ConfigMap createConfigMap(String namespace) {
        return new ConfigMapBuilder()
                .withImmutable(true)
                .withNewMetadata().withName("libfaketime").withNamespace(namespace).endMetadata()
                .addToBinaryData("bin/faketime", faketime)
                .addToBinaryData("lib/faketime/libfaketime.so.1", libfaketime)
                .addToBinaryData("lib/faketime/libfaketimeMT.so.1", libfaketimeMT)
                .build();
    }

    private String read(String path) {
        try {
            byte[] contents = FileUtils.readFileToByteArray(new File(mountPath + path));
            return Base64.getEncoder().encodeToString(contents);
        }
        catch (IOException e) {
            Log.error("Cannot open file.", e);
            throw new IllegalArgumentException("Cannot open file.", e);
        }
    }
}
