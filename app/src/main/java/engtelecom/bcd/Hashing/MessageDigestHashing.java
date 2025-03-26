package engtelecom.bcd.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class MessageDigestHashing implements PasswordHashing {

    private static final SecureRandom S_RANDOM = new SecureRandom();
    private final byte[] salt;
    private final String algorithm;

    public MessageDigestHashing(String algorithm) {
        this.salt = new byte[16];
        S_RANDOM.nextBytes(this.salt);
        this.algorithm = algorithm;
    }

    @Override
    public String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(salt);
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("❌ Erro ao gerar hash MessageDigest.", e);
        }
    }

    @Override
    public boolean verify(String password, String hashedPassword) {
        String hashGenerated = hash(password);
        return hashGenerated.equals(hashedPassword);
    }
}
