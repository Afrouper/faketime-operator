package de.afrouper.faketime.impl;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FaketimeConfigMapTest {

    @Inject
    FaketimeConfigMap faketimeConfigMap;

    @Test
    public void configMap() {
        ConfigMap configMap = faketimeConfigMap.createConfigMap("NSP-foo");
        Assertions.assertNotNull(configMap);

        Assertions.assertEquals("libfaketime", configMap.getMetadata().getName(), "ConfigMap name wrong");
        Assertions.assertTrue(configMap.getImmutable());
        Assertions.assertEquals("NSP-foo", configMap.getMetadata().getNamespace());
        Assertions.assertEquals(3, configMap.getBinaryData().size(), "Wrong number of binary contents");
        Assertions.assertEquals(0, configMap.getData().size(), "Non-binary data should be empty.");

        byte[] decode = Base64.getDecoder().decode(configMap.getBinaryData().get("bin/faketime"));
        Assertions.assertEquals("Simple binary Testfile 'faketime' to checkin", new String(decode));

        decode = Base64.getDecoder().decode(configMap.getBinaryData().get("lib/faketime/libfaketime.so.1"));
        Assertions.assertEquals("Simple binary Testfile 'libfaketime.so.1' to checkin", new String(decode));

        decode = Base64.getDecoder().decode(configMap.getBinaryData().get("lib/faketime/libfaketimeMT.so.1"));
        Assertions.assertEquals("Simple binary Testfile 'libfaketimeMT.so.1' to checkin", new String(decode));
    }
}