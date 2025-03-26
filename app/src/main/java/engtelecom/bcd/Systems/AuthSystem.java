package engtelecom.bcd.Systems;
 

import engtelecom.bcd.Hashing.PasswordHashing;
import engtelecom.bcd.Models.User;
import engtelecom.bcd.Repository.RepositoryCSV;

/**
 * Classe que implementa um serviço de autenticação.
 */
public class AuthSystem {
    private final RepositoryCSV<User> repository;
    private final PasswordHashing passwordHashing;

    /**
     * Construtor do sistema de autenticação.
     *
     * @param algorithm Nome do algoritmo de hash (ex: PBKDF2, SHA-256, etc.)
     */
    public AuthSystem(String algorithm) {
        this.repository = new RepositoryCSV<>("db/users.csv", User.class, "userId");
        this.passwordHashing = PasswordHashing.defineAlgorithmType(algorithm);
    }

/**
 * Registra um novo usuário no sistema.
 *
 * @param login Login do usuário
 * @param password Senha do usuário
 * @param email Email do usuário
 * @param accountBalance Saldo da conta do usuário
 * @return true se o registro for bem-sucedido, false se o login já existir
 */
public boolean register(String login, String password, String email, double accountBalance) {
    // Verifica se já existe um usuário com esse login
    if (repository.selectFirstWhere("login", login) != null) {
        return false;
    }

    String userId = "USR-" + Math.abs((login + System.currentTimeMillis()).hashCode());
    String hashedPassword = passwordHashing.hash(password);
    User newUser = new User(userId, login, hashedPassword, email, accountBalance);

    return repository.insert(newUser);
}


    /**
     * Atualiza a senha de um usuário já cadastrado.
     *
     * @param login Login do usuário
     * @param newPassword Nova senha
     * @param confirmNewPassword Confirmação da nova senha
     * @return true se a senha foi atualizada com sucesso, false caso contrário
     */
    public boolean updatePassword(String login, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) return false;
        User user = repository.selectFirstWhere("login", login);
        if (user == null) return false;
    
        String hashedNewPassword = passwordHashing.hash(newPassword);
    
        return repository.updateField(user.getUserId(), "hashedPassword", hashedNewPassword);
    }
    

    /**
     * Autentica um usuário com login e senha.
     *
     * @param login Login do usuário
     * @param password Senha fornecida
     * @return true se as credenciais estiverem corretas, false caso contrário
     */
    public boolean authenticate(String login, String password) {
        User user = repository.selectFirstWhere("login", login);
        if (user == null) return false;
        return passwordHashing.verify(password, user.getHashedPassword());
    }

   /**
     * Retorna um usuário a partir do seu ID.
     */
    public User getUserById(String userId) {
        return repository.selectById(userId);
    }

    /**
    /**
     * Retorna o usuário com base no login.
     */
    public User getUserByLogin(String login) {
        return repository.selectFirstWhere("login", login);
    }


    /**
     * Remove um usuário do sistema com base no login (ID).
     */
    public boolean removeUser(String login) {
        return repository.deleteById(login);
    }
}
