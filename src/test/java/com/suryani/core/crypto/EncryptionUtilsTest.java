package com.zyd.core.crypto;

import com.zyd.core.util.ClasspathResource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class EncryptionUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void encrypt() {
        String encryptedText = EncryptionUtils.encrypt("test-string", new ClasspathResource("crypto/encryption-public.key"));

        Assert.assertEquals("test-string", EncryptionUtils.decrypt(encryptedText, new ClasspathResource("crypto/encryption-private.key")));
    }

    @Test
    public void failedToDecrypt() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("failed to decrypt");

        EncryptionUtils.decrypt("caKcsLQW/ID/2p/Snhbzx2O7qMWKuFA5jj66g51gAjt+Mwpkpi2xgHrw0wYcRXQl5jEYSfmFq0OaueCTMSt9HMWpNgldo/3KQNA5yguUduA7a1MWeFUMalCoNS0FToXpK/aUznqnSBpsW3yLm1YfUQAvVJJRbN//OqzcvSdZSUuQN+c7jvsSMVf0ra+Z6smaRlbbExI3s9abLIVMPEfkedJ//+eGPE+jS8oagJ23nCrtzw0hlAa6IsV6/Red0/EH3SY2mTFPeBM3UN4epok9sNAXFnfJ3QzB+L1JgN+Fd/D4kCUdAnnZABJDgPtzyzxekeUe+2fGmAA7GRDNdb7Tfw==",
                new ClasspathResource("crypto/encryption-private.key"));
    }
}
