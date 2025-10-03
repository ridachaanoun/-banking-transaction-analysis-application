package serveses;
import entity.*;

import serveses.impl.AccountServiceImpl;
import serveses.impl.ClientServiceImpl;
import serveses.impl.RapportServiceImpl;
import serveses.impl.TransactionServiceImpl;
import ui.Input;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleService {
    
    private final ClientService clientService = new ClientServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransactionService transactionService = new TransactionServiceImpl();
    private final RapportService rapportService = new RapportServiceImpl();

    public void listAllClients() throws Exception {
        List <Client> clients = clientService.listAllClients();
        if (clients.isEmpty()) System.out.println("NO clients found: ");
        clients.forEach(c-> System.out.println(c.id() + "|" + c.name() + "|"+ c.email() ));
    }

    public void addclient() throws Exception{
        String name = Input.prompt("name: ");
        String email = Input.prompt("email: ");
        clientService.addClient(new Client(0,name,email));
        System.out.println("Client added.");
    } 

    public void updateClient()throws Exception{
        int id = Input.promptInt("Client ID to update: ");
        Client client = clientService.findClientById(id);
        String name = Input.prompt("name ("+ client.name()+"): ");
        if(name.isEmpty())name = client.name();
        String email = Input.prompt("email("+client.email()+"): ");
        if(email.isEmpty())email=client.email();
        clientService.updateClient(new Client( id ,name , email));
        System.out.println("Client updated");
    }

    public void deleteClient() throws Exception{
        int id = Input.promptInt("Client ID to delete: ");
        Client client = clientService.findClientById(id);
        boolean confirm = Input.promptYesNo("Are you sure you want to delete client " + client.name() + "?", false);
        if(confirm == true){
             clientService.deleteClient(id);
             System.out.println("client deleted");
        }else{
            System.out.println("delete canceled");
        }

    }

    public void findClinetByid()throws Exception{
        int id = Input.promptInt("Client ID: ");
        Client c = clientService.findClientById(id);
        if (c == null) System.out.println("Not found.");
        else System.out.println(c.id() + " | " + c.name() + " | " + c.email());
    }
    public void findClientsByName() throws Exception {
        String q = Input.prompt("Name contains: ");
        List<Client> list = clientService.findClientsByName(q);
        if (list.isEmpty()) System.out.println("No clients match.");
        list.forEach(c -> System.out.println(c.id() + " | " + c.name() + " | " + c.email()));
    }

    public void clientSummary() throws Exception {
        int id = Input.promptInt("Client ID: ");
        double total = clientService.getTotalBalanceForClient(id);
        Account max = clientService.getMaxBalanceAccountForClient(id);
        Account min = clientService.getMinBalanceAccountForClient(id);
        System.out.println("Total balance: " + total);
        System.out.println("Max account: " + (max == null ? "-" : max.getNumber() + " (" + max.getBalance() + ")"));
        System.out.println("Min account: " + (min == null ? "-" : min.getNumber() + " (" + min.getBalance() + ")"));
    }
    public void listAllAccounts ()throws Exception {
        List<Account> accounts = accountService.listAllAccounts();
        if(accounts.isEmpty())System.out.println("no accounts found");
        accounts.forEach(this::printAccount);
    } 

    public void createAccount() throws Exception {
        System.out.println("Type: 1) Current  2) Savings");
        int t = Input.promptInt("Choose: ");
        String number = Input.prompt("Account number: ");
        double balance = Input.promptDouble("Initial balance: ");
        int clientId = Input.promptInt("Client ID: ");

        switch (t) {
            case 1 -> {
                double overdraft = Input.promptDouble("Overdraft limit: ");
                Account acc = new CurrentAccount(0, number, balance, clientId, overdraft);
                accountService.createAccount(acc);
            }
            case 2 -> {
                double rate = Input.promptDouble("Interest rate: ");
                Account acc = new SavingsAccount(0, number, balance, clientId, rate);
                accountService.createAccount(acc);
            }
            default -> {
                System.out.println("Invalid type.");
                return;
            }
        }
        System.out.println("Account created.");
    }

    public void updateAccount() throws Exception {
        int id = Input.promptInt("Account ID to update: ");
        Account acc = accountService.findAccountById(id);
        if (acc == null) {
            System.out.println("Account not found.");
            return;
        }
        String number = Input.prompt("Number (" + acc.getNumber() + "): ");
        if (!number.isEmpty()) acc.setNumber(number);
        String nb = Input.prompt("Balance (" + acc.getBalance() + "): ");
        if (!nb.isEmpty()) acc.setBalance(Double.parseDouble(nb));

        if (acc instanceof CurrentAccount ca) {
            String s = Input.prompt("Overdraft limit (" + ca.getOverdraftLimit() + "): ");
            if (!s.isEmpty()) ca.setOverdraftLimit(Double.parseDouble(s));
        } else if (acc instanceof SavingsAccount sa) {
            String s = Input.prompt("Interest rate (" + sa.getInterestRate() + "): ");
            if (!s.isEmpty()) sa.setInterestRate(Double.parseDouble(s));
        }

        accountService.updateAccount(acc);
        System.out.println("Account updated.");
    }

    public void deleteAccount() throws Exception {
        int id = Input.promptInt("Account ID to delete: ");
        accountService.deleteAccount(id);
        System.out.println("Account deleted.");
    }

    public void findAccountById() throws Exception {
        int id = Input.promptInt("Account ID: ");
        Account acc = accountService.findAccountById(id);
        if (acc == null) System.out.println("Not found.");
        else printAccount(acc);
    }

    public void findAccountsByClient() throws Exception {
        int clientId = Input.promptInt("Client ID: ");
        List<Account> list = accountService.findAccountsByClientId(clientId);
        if (list.isEmpty()) System.out.println("No accounts for this client.");
        list.forEach(this::printAccount);
    }

    public void printAccount(Account acc) {
        String type = (acc instanceof CurrentAccount) ? "CURRENT" : (acc instanceof SavingsAccount ? "SAVINGS" : "UNKNOWN");
        String extra = (acc instanceof CurrentAccount ca)
                ? (" | overdraft=" + ca.getOverdraftLimit())
                : (acc instanceof SavingsAccount sa ? (" | rate=" + sa.getInterestRate()) : "");
        System.out.println(acc.getId() + " | " + type + " | " + acc.getNumber() + " | bal=" + acc.getBalance() + " | client=" + acc.getClientId() + extra);
    }

    public void listAllTransactions() throws Exception {
        List<Transaction> list = transactionService.listAllTransactions();
        printTransactions(list);
    }

    public void listTransactionsByAccount() throws Exception {
        int accId = Input.promptInt("Account ID: ");
        List<Transaction> list = transactionService.listTransactionsByAccount(accId);
        printTransactions(list);
    }

    public void listTransactionsByClient() throws Exception {
        int clientId = Input.promptInt("Client ID: ");
        List<Transaction> list = transactionService.listTransactionsByClient(clientId);
        printTransactions(list);
    }

    public void addTransaction() throws Exception {
        int accountId = Input.promptInt("Account ID: ");
        LocalDate date = Input.promptDate("Date", LocalDate.now());
        double amount = Input.promptDouble("Amount: ");
        System.out.println("Type: 1) DEPOSIT  2) WITHDRAWAL  3) TRANSFER");
        int t = Input.promptInt("Choose: ");
        TransactionType type = switch (t) {
            case 1 -> TransactionType.DEPOSIT;
            case 2 -> TransactionType.WITHDRAWAL;
            case 3 -> TransactionType.TRANSFER;
            default -> null;
        };
        if (type == null) {
            System.out.println("Invalid type.");
            return;
        }
        String location = Input.prompt("Location (optional): ");
        Transaction tx = new Transaction(0, date, amount, type, location.isEmpty() ? null : location, accountId);
        transactionService.addTransaction(tx);
        System.out.println("Transaction added.");
    }

    public void filterTransactions() throws Exception {
        System.out.println("Filtering all transactions (in-memory). Leave blank to skip a filter.");
        String minS = Input.prompt("Min amount: ");
        String maxS = Input.prompt("Max amount: ");
        String typeS = Input.prompt("Type (DEPOSIT/WITHDRAWAL/TRANSFER): ");
        String fromS = Input.prompt("From date (yyyy-MM-dd): ");
        String toS = Input.prompt("To date (yyyy-MM-dd): ");
        String locationS = Input.prompt("Location contains: ");

        Double min = minS.isEmpty() ? null : Double.parseDouble(minS);
        Double max = maxS.isEmpty() ? null : Double.parseDouble(maxS);
        TransactionType type = typeS.isEmpty() ? null : TransactionType.valueOf(typeS.trim().toUpperCase());
        LocalDate from = fromS.isEmpty() ? null : LocalDate.parse(fromS);
        LocalDate to = toS.isEmpty() ? null : LocalDate.parse(toS);

        List<Transaction> all = transactionService.listAllTransactions();
        List<Transaction> filtered = transactionService.filterTransactions(all, min, max, type, from, to, locationS);
        printTransactions(filtered);
    }

    
    public void deleteTransaction() throws Exception {
        int id = Input.promptInt("Transaction ID: ");
        transactionService.deleteTransaction(id);
        System.out.println("Transaction deleted.");
    }

    public void printTransactions(List<Transaction> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No transactions.");
            return;
        }
        list.forEach(t ->
                System.out.println(t.id() + " | " + t.date() + " | " + t.type() + " | " + t.amount() + " | acc=" + t.accountId() + " | " + (t.location() == null ? "-" : t.location()))
        );
        double total = transactionService.totalAmount(list);
        double avg = transactionService.averageAmount(list);
        System.out.println("Total: " + total + " | Average: " + avg);
    }

    public void reportTopClients() throws Exception {
        int n = Input.promptInt("How many top clients? ");
        List<Client> top = rapportService.generateTopClients(n);
        if (top.isEmpty()) {
            System.out.println("No data.");
            return;
        }
        for (int i = 0; i < top.size(); i++) {
            Client c = top.get(i);
            System.out.println((i + 1) + ") " + c.id() + " | " + c.name() + " | " + c.email());
        }
    }

    public void reportMonthly() throws Exception {
        int year = Input.promptInt("Year (e.g., 2025): ");
        var report = rapportService.generateMonthlyReport(year);
        if (report.isEmpty()) {
            System.out.println("No data for year " + year);
            return;
        }
        for (var entry : report.entrySet()) {
            YearMonth ym = entry.getKey();
            Map<TransactionType, Double> totals = entry.getValue();
            String line = Arrays.stream(TransactionType.values())
                    .map(tt -> tt.name() + "=" + totals.getOrDefault(tt, 0.0))
                    .collect(Collectors.joining(", "));
            System.out.println(ym + " => " + line);
        }
    }

    public void reportInactiveAccounts() throws Exception {
        int days = Input.promptInt("Inactive if no activity in last N days: ");
        List<Account> inactive = rapportService.findInactiveAccounts(days);
        if (inactive.isEmpty()) {
            System.out.println("No inactive accounts.");
            return;
        }
        inactive.forEach(this::printAccount);
    }

    public void reportSuspicious() throws Exception {
        double threshold = Input.promptDouble("High amount threshold: ");
        String usual = Input.prompt("Usual locations (comma-separated, leave blank for none): ");
        Set<String> usualSet = usual.isBlank()
                ? Collections.emptySet()
                : Arrays.stream(usual.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        int burst = Input.promptInt("Burst threshold (transactions per day per account): ");
        List<Transaction> sus = rapportService.detectSuspiciousTransactions(threshold, usualSet, burst);
        if (sus.isEmpty()) System.out.println("No suspicious transactions.");
        else printTransactions(sus);
    }
}
