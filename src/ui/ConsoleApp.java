package ui;

import serveses.ConsoleService;

public class ConsoleApp {

    private final ConsoleService consoleService = new ConsoleService();


    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Banking Console ===");
            System.out.println("1) Clients");
            System.out.println("2) Accounts");
            System.out.println("3) Transactions");
            System.out.println("4) Reports");
            System.out.println("0) Exit");
            int choice = Input.promptInt("Choose: ");
            try {
                switch (choice) {
                    case 1 -> clientsMenu();
                    case 2 -> accountsMenu();
                    case 3 -> transactionsMenu();
                    case 4 -> reportsMenu();
                    case 0 -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    // Clients

    private void clientsMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Clients Menu ---");
            System.out.println("1) List all clients");
            System.out.println("2) Add client");
            System.out.println("3) Update client");
            System.out.println("4) Delete client");
            System.out.println("5) Find client by ID");
            System.out.println("6) Find clients by name");
            System.out.println("7) Client balance summary");
            System.out.println("0) Back");
            int c = Input.promptInt("Choose: ");
            switch (c) {
                case 1 -> consoleService.listAllClients();
                case 2 -> consoleService.addclient();
                case 3 -> consoleService.updateClient();
                case 4 -> consoleService.deleteClient();
                case 5 -> consoleService.findClinetByid();
                case 6 -> consoleService.findClientsByName();
                case 7 -> consoleService.clientSummary();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    // Accounts

    private void accountsMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Accounts Menu ---");
            System.out.println("1) List all accounts");
            System.out.println("2) Create account");
            System.out.println("3) Update account");
            System.out.println("4) Delete account");
            System.out.println("5) Find account by ID");
            System.out.println("6) Find accounts by Client ID");
            System.out.println("0) Back");
            int c = Input.promptInt("Choose: ");
            switch (c) {
                case 1 -> consoleService.listAllAccounts();
                case 2 -> consoleService.createAccount();
                case 3 -> consoleService.updateAccount();
                case 4 -> consoleService.deleteAccount();
                case 5 -> consoleService.findAccountById();
                case 6 -> consoleService.findAccountsByClient();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    // Transactions

    private void transactionsMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Transactions Menu ---");
            System.out.println("1) List all transactions");
            System.out.println("2) List by account");
            System.out.println("3) List by client");
            System.out.println("4) Add transaction");
            System.out.println("5) Delete transaction");
            System.out.println("6) Filter by amount/type/date/location (in-memory)");
            System.out.println("0) Back");
            int c = Input.promptInt("Choose: ");
            switch (c) {
                case 1 -> consoleService.listAllTransactions();
                case 2 -> consoleService.listTransactionsByAccount();
                case 3 -> consoleService.listTransactionsByClient();
                case 4 -> consoleService.addTransaction();
                case 5 -> consoleService.deleteTransaction();
                case 6 -> consoleService.filterTransactions();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    // Reports

    private void reportsMenu() throws Exception {
        while (true) {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("1) Top N clients by total balance");
            System.out.println("2) Monthly totals by type for a given year");
            System.out.println("3) Find inactive accounts (by last activity)");
            System.out.println("4) Detect suspicious transactions");
            System.out.println("0) Back");
            int c = Input.promptInt("Choose: ");
            switch (c) {
                case 1 -> consoleService.reportTopClients();
                case 2 -> consoleService.reportMonthly();
                case 3 -> consoleService.reportInactiveAccounts();
                case 4 -> consoleService.reportSuspicious();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}