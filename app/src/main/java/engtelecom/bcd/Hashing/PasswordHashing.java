package engtelecom.bcd.Hashing;

/**
 * Interface para classes que implementam algoritmos de hashing de senha.
 */
public interface PasswordHashing {

    /**
     * Gera o hash da senha como String  
     */
    String hash(String password);

    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado
     */
    boolean verify(String password, String hashedPassword);

    static PasswordHashing defineAlgorithmType(String algorithm) {
        return switch (algorithm) {
            case "PBKDF2WithHmacSHA1", "PBKDF2WithHmacSHA256", "PBKDF2WithHmacSHA512" ->
                new Pbkdf2Hashing(algorithm);
            case "MD5", "SHA-1", "SHA-256", "SHA-512" ->
                new MessageDigestHashing(algorithm);
            case "BCrypt" ->
                new BcryptHashing();
            default ->
                throw new IllegalStateException("❌ Erro: Algoritmo " + algorithm + " não é suportado.");
        };
    }
}
