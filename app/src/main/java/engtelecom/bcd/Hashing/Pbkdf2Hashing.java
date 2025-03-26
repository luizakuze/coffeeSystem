package engtelecom.bcd.Hashing;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pbkdf2Hashing implements PasswordHashing {

    private static final SecureRandom S_RANDOM = new SecureRandom();
    private final int ITERATIONS = 210000;
    private final int KEYLENGTH = 128;
    private final byte[] salt;
    private final String algorithm;

    public Pbkdf2Hashing(String algorithm) {
        this.salt = new byte[16];
        S_RANDOM.nextBytes(this.salt);
        this.algorithm = algorithm;
    }

    @Override
    public String hash(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLENGTH);
        try {
            byte[] hashed = SecretKeyFactory.getInstance(algorithm).generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashed);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("❌ Erro ao gerar hash PBKDF2.", e);
        }
    }

    @Override
    public boolean verify(String password, String hashedPassword) {
        String hashGenerated = hash(password);
        return hashGenerated.equals(hashedPassword);
    }
}
