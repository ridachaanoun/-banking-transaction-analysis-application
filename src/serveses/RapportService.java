package serveses;

import entity.Account;
import entity.Client;
import entity.Transaction;
import entity.TransactionType;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RapportService {
    // Top clients by total balance across all their accounts
    List<Client> generateTopClients(int limit) throws Exception;

    // Monthly totals per transaction type for a specific year
    Map<YearMonth, Map<TransactionType, Double>> generateMonthlyReport(int year) throws Exception;

    // Detect suspicious transactions using simple heuristics
    List<Transaction> detectSuspiciousTransactions(
            double highAmountThreshold,
            Set<String> usualLocationsOrCountries,
            int burstCountThreshold
    ) throws Exception;

    // Accounts with no transactions in the last N days (or no transactions at all)
    List<Account> findInactiveAccounts(int inactiveDaysThreshold) throws Exception;
}