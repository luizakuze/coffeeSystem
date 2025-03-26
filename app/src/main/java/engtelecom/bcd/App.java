package engtelecom.bcd;

import java.util.Scanner;

import engtelecom.bcd.Models.CoffeeType;
import engtelecom.bcd.Models.User;
import engtelecom.bcd.Systems.AuthSystem;
import engtelecom.bcd.Systems.CoffeeSystem;
import engtelecom.bcd.Systems.InvoiceSystem;

/**
 * Classe principal
 */
public class App {
    static Scanner scanner = new Scanner(System.in);

    private AuthSystem authSystem;
    private CoffeeSystem coffeeSystem;
    private InvoiceSystem inVoiceSystem;

    public void initializeAuthSystem() {
        this.authSystem = new AuthSystem("BCrypt");
        this.coffeeSystem = new CoffeeSystem();
        this.inVoiceSystem = new InvoiceSystem();

        authMenu();
    }

    /**
     * Menu inicial de autenticação
     */
    private void authMenu() {
        while (true) {
            System.out.println(" ----- Sistema de Autenticação ----- ");
            System.out.println(" (1) Registrar um novo usuário ");
            System.out.println(" (2) Atualizar a senha de um usuário ");
            System.out.println(" (3) Autenticar e acessar o sistema ");
            System.out.println(" (4) Sair ");
            System.out.print("Escolha uma opção (1..4) > ");

            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1:
                    System.out.print("Digite seu login > ");
                    String login1 = scanner.nextLine();
                    System.out.print("Digite sua senha > ");
                    String password1 = scanner.nextLine();
                    System.out.print("Digite seu email > ");
                    String email = scanner.nextLine();
                    System.out.print("Saldo inicial da conta > ");
                    double saldo = scanner.nextDouble();
                    scanner.nextLine();
                    if (authSystem.register(login1, password1, email, saldo))
                        System.out.println("✅ Usuário registrado com sucesso!");
                    else
                        System.out.println("❌ Este login já existe.");
                    break;
            
                case 2:
                    System.out.print("Digite seu login > ");
                    String login2 = scanner.nextLine();
                    System.out.print("Digite sua senha > ");
                    String password2 = scanner.nextLine();
                    if (authSystem.authenticate(login2, password2)) {
                        System.out.print("Nova senha > ");
                        String nova = scanner.nextLine();
                        System.out.print("Confirme a nova senha > ");
                        String conf = scanner.nextLine();
                        if (authSystem.updatePassword(login2, nova, conf)) {
                            System.out.println("✅ Senha atualizada!");
                        } else {
                            System.out.println("❌ Erro ao atualizar a senha.");
                        }
                    } else {
                        System.out.println("❌ Autenticação falhou.");
                    }
                    break;
           
                case 3:
                    System.out.print("Digite seu login > ");
                    String login3 = scanner.nextLine();
                    System.out.print("Digite sua senha > ");
                    String password3 = scanner.nextLine();
                    if (authSystem.authenticate(login3, password3)) {
                        System.out.println("✅ Autenticado! Acessando sistema...");
                        User user = authSystem.getUserByLogin(login3);
                        if (user != null) {
                            coffeeMenu(user.getUserId());
                        } else {
                            System.out.println("❌ Erro ao carregar usuário.");
                        }
                    } else {
                        System.out.println("❌ Autenticação falhou.");
                    }
                    break;
            
                case 4:
                    System.out.println("Finalizando Sistema... 👋👋👋");
                    return;
            
                default:
                    System.out.println("❌ Opção inválida!");
            }
            
        }
    }

    /**
     * Menu do sistema de cafeteria (após autenticação)
     */
    private void coffeeMenu(String userId) {
        boolean continua = true;

        while (continua) {
            System.out.println("\n ----- Cafeteira ----- ");
            System.out.println("1. Informações de usuário");
            System.out.println("2. Informações da cafeteira");
            System.out.println("3. Histórico de cafés");
            System.out.println("4. Servir café");
            System.out.println("5. Reabastecer cafeteira");
            System.out.println("6. Remover minha conta");
            System.out.println("7. Sair");
            System.out.print("Sua opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println(authSystem.getUserById(userId));
                    break;

                case 2:
                    System.out.println(coffeeSystem.getStatus());
                    break;

                case 3:
                    System.out.println("📋 Histórico de cafés:");
                    System.out.println("1. Ver todas as transações");
                    System.out.println("2. Ver transações por mês");
                    System.out.print("Escolha uma opção: ");
                    int escolha = scanner.nextInt();
                    scanner.nextLine();

                    if (escolha == 1) {
                        inVoiceSystem.history(userId, null); // mostra tudo
                    } else if (escolha == 2) {
                        System.out.print("Digite o mês com dois dígitos (ex: '03' para março): ");
                        String mes = scanner.nextLine();
                        inVoiceSystem.history(userId, mes); // filtra por mês
                    } else {
                        System.out.println("❌ Opção inválida.");
                    }
                    break;

                case 4:
                    coffeeSystem.getCoffeeMenu();
                    System.out.print("Escolha o número do café desejado: ");
                    int numero = scanner.nextInt();
                    scanner.nextLine();
                    
                    CoffeeType tipo = coffeeSystem.getCoffeeByMenuNumber(numero);
                    if (tipo == null) {
                        System.out.println("❌ Tipo de café inválido.");
                        break;
                    }
                    
                    boolean feito = coffeeSystem.makeCoffee(tipo.getId(), authSystem.getUserById(userId));
                    if (feito) {
                        inVoiceSystem.addInvoice(authSystem.getUserById(userId), tipo); 
                        System.out.println("✅ Café servido!");
                    } else {
                        System.out.println("❌ Falha ao servir café.");
                    }
                    break;

                case 5:
                    coffeeSystem.refillCoffee();
                    System.out.println("☕ Cafeteira reabastecida!");
                    break;

                case 6:
                    System.out.print("Tem certeza que deseja remover sua conta? (s/n): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("s")) {
                        boolean removed = authSystem.removeUser(userId);
                        if (removed) {
                            System.out.println("✅ Conta removida com sucesso. Até a próxima!");
                            continua = false;  
                        } else {
                            System.out.println("❌ Não foi possível remover a conta.");
                        }
                    } else {
                        System.out.println("❎ Ação cancelada.");
                    }
                    break;
            
                case 7:
                    continua = false;
                    System.out.println("👋 Encerrando sessão...");
                    break;

                default:
                    System.out.println("❌ Opção inválida.");
                    break;
            }
        }
    }


    public static void main(String[] args) {
        App app = new App();
        app.initializeAuthSystem();
    }
}
