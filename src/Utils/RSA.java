package Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSA {
    
    private static String ALGORITHM = "RSA";
    
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
    
    KeyPairGenerator keyGen;

    /**
     * Creates a new instance of <code>RSA</code>
     * 
     * @throws NoSuchAlgorithmException 
     */
    public RSA() throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024, new SecureRandom());
        KeyPair keyPair=keyGen.generateKeyPair();
        publicKey = (RSAPublicKey)keyPair.getPublic();
        privateKey = (RSAPrivateKey)keyPair.getPrivate();
    }
    
}
