package engtelecom.bcd.Models;

/**
 * Classe que representa um usuário do sistema.
 */
public class User {
    private String userId;
    private String login;
    private String hashedPassword;
    private String email;
    private double accountBalance;

    public User() {
    }

    public User(String userId, String login, String hashedPassword, String email, double accountBalance) {
        this.userId = userId;
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.accountBalance = accountBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUser_id(String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "👤 Usuário: " + login + "\n📧 Email: " + email + "\n💰 Saldo: R$" + accountBalance;
    }
}
