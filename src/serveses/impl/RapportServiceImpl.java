package serveses.impl;

import dao.AccountDAO;
import dao.ClientDAO;
import dao.TransactionDAO;
import dao.impl.AccountDAOImpl;
import dao.impl.ClientDAOImpl;
import dao.impl.TransactionDAOImpl;
import entity.Account;
import entity.Client;
import entity.Transaction;
import entity.TransactionType;
import serveses.RapportService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class RapportServiceImpl implements RapportService {

    private final ClientDAO clientDAO = new ClientDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();

    @Override
    public List<Client> generateTopClients(int limit) throws Exception {
        // Sum balances per clientId
        Map<Integer, Double> totalByClient = accountDAO.findAllAccounts().stream()
                .collect(Collectors.groupingBy(
                        Account::getClientId,
                        Collectors.summingDouble(Account::getBalance)
                ));

        // Sort by total balance desc and map to Client
        List<Integer> sortedClientIds = totalByClient.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();

        List<Client> result = new ArrayList<>();
        for (Integer clientId : sortedClientIds) {
            Client c = clientDAO.findClientById(clientId);
            if (c != null) result.add(c);
        }
        return result;
    }

    @Override
    public Map<YearMonth, Map<TransactionType, Double>> generateMonthlyReport(int year) throws Exception {
        // Group transactions by YearMonth, then by type, summing amounts
        Map<YearMonth, Map<TransactionType, Double>> report = new TreeMap<>();
        for (Transaction t : transactionDAO.findAllTransactions()) {
            if (t.date().getYear() != year) continue;

            YearMonth ym = YearMonth.from(t.date());
            report.computeIfAbsent(ym, k -> new EnumMap<>(TransactionType.class));
            Map<TransactionType, Double> typeTotals = report.get(ym);

            typeTotals.merge(t.type(), t.amount(), Double::sum);
        }
        return report;
    }

    @Override
    public List<Transaction> detectSuspiciousTransactions(
            double highAmountThreshold,
            Set<String> usualLocationsOrCountries,
            int burstCountThreshold
    ) throws Exception {
        List<Transaction> all = transactionDAO.findAllTransactions();
        List<Transaction> suspicious = new ArrayList<>();

        // Rule 1: high amount
        suspicious.addAll(all.stream()
                .filter(t -> t.amount() >= highAmountThreshold)
                .toList());

        // Rule 2: unusual location
        if (usualLocationsOrCountries != null && !usualLocationsOrCountries.isEmpty()) {
            suspicious.addAll(all.stream()
                    .filter(t -> t.location() != null && !usualLocationsOrCountries.contains(t.location()))
                    .toList());
        }

        // Rule 3: burst frequency per account (LocalDate granularity -> "same-day" bursts)
        Map<Integer, List<Transaction>> byAccount = all.stream()
                .collect(Collectors.groupingBy(Transaction::accountId));

        for (List<Transaction> list : byAccount.values()) {
            Map<LocalDate, Long> byDay = list.stream()
                    .collect(Collectors.groupingBy(Transaction::date, Collectors.counting()));
            byDay.forEach((day, count) -> {
                if (count >= burstCountThreshold) {
                    suspicious.addAll(list.stream().filter(t -> t.date().equals(day)).toList());
                }
            });
        }

        // Deduplicate and return
        return suspicious.stream().distinct().toList();
    }

    @Override
    public List<Account> findInactiveAccounts(int inactiveDaysThreshold) throws Exception {
        LocalDate cutoff = LocalDate.now().minusDays(inactiveDaysThreshold);
        List<Account> allAccounts = accountDAO.findAllAccounts();
        List<Account> inactive = new ArrayList<>();

        for (Account acc : allAccounts) {
            List<Transaction> txs = transactionDAO.findTransactionsByAccountId(acc.getId());
            if (txs.isEmpty()) {
                inactive.add(acc);
                continue;
            }
            LocalDate lastDate = txs.stream()
                    .map(Transaction::date)
                    .max(LocalDate::compareTo)
                    .orElse(null);
            if (lastDate == null || lastDate.isBefore(cutoff)) {
                inactive.add(acc);
            }
        }
        return inactive;
    }
}