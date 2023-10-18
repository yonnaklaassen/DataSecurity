package Shared;

import javax.crypto.KeyAgreement;
import java.security.*;

public class DH {
    public static void main(String[] args) throws Exception {
        // Alice and Bob generate their own key pairs
        KeyPair aliceKeyPair = generateKeyPair();
        KeyPair bobKeyPair = generateKeyPair();
        System.out.println(bobKeyPair.getPublic().getAlgorithm());
        // Alice and Bob exchange their public keys
        PublicKey alicePublicKey = aliceKeyPair.getPublic();
        PublicKey bobPublicKey = bobKeyPair.getPublic();

        // Alice and Bob generate their shared secrets
        byte[] sharedSecretAlice = generateSharedSecret(aliceKeyPair.getPrivate(), bobPublicKey);
        byte[] sharedSecretBob = generateSharedSecret(bobKeyPair.getPrivate(), alicePublicKey);

        // Verify that the shared secrets are the same
        System.out.println("Shared secrets match: " + MessageDigest.isEqual(sharedSecretAlice, sharedSecretBob));
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        System.out.println("Key agreement"+keyAgreement);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        return keyAgreement.generateSecret();
    }
}
