package com.zyd.core.crypto;

import com.zyd.core.TestResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;

/**
 * @author neo
 */
public class SignatureTest {
    Signature signature;

    @Before
    public void createSignature() {
        signature = new Signature();
    }

    @Test
    public void signWithJavaGeneratedPrivateKey() {
        KeyPair keyPair = signature.generateKeyPair();
        signature.setPrivateKey(keyPair.getPrivate().getEncoded());
        signature.setPublicKey(keyPair.getPublic().getEncoded());
        String message = "test message";

        byte[] sign = signature.sign(message.getBytes());

        boolean valid = signature.verify(message.getBytes(), sign);
        Assert.assertTrue(valid);
    }

    @Test
    public void signWithOpenSSLGeneratedCert() {
        byte[] cert = TestResource.bytes("/crypto/signature-cert.der");
        byte[] privateKey = TestResource.bytes("/crypto/signature-private.der");

        signature.setPrivateKey(privateKey);
        signature.setCertificate(cert);
        String message = "test message";

        byte[] sign = signature.sign(message.getBytes());

        boolean valid = signature.verify(message.getBytes(), sign);
        Assert.assertTrue(valid);
    }

    @Test
    public void invalidSignature() {
        byte[] cert = TestResource.bytes("/crypto/signature-cert-not-match.der");
        byte[] privateKey = TestResource.bytes("/crypto/signature-private.der");

        signature.setPrivateKey(privateKey);
        signature.setCertificate(cert);
        String message = "test message";

        byte[] sign = signature.sign(message.getBytes());
        boolean valid = signature.verify(message.getBytes(), sign);
        Assert.assertFalse(valid);
    }
}
