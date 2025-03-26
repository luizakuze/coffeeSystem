package engtelecom.bcd.Hashing;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptHashing implements PasswordHashing {

    @Override
    public String hash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public boolean verify(String password, String hashedPassword) {
        return new BCryptPasswordEncoder().matches(password, hashedPassword);
    }
}
