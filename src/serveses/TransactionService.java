package serveses;

import entity.Transaction;
import entity.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TransactionService {
    // CRUD
    void addTransaction(Transaction t) throws Exception;
    void updateTransaction(Transaction t) throws Exception;
    void deleteTransaction(int id) throws Exception;

    Transaction findTransactionById(int id) throws Exception;
    List<Transaction> listTransactionsByAccount(int accountId) throws Exception;
    List<Transaction> listTransactionsByClient(int clientId) throws Exception;
    List<Transaction> listAllTransactions() throws Exception;

    // Filters and grouping
    List<Transaction> filterTransactions(
            List<Transaction> source,
            Double minAmount,
            Double maxAmount,
            TransactionType type,
            LocalDate fromDate,
            LocalDate toDate,
            String locationContains
    );

    Map<TransactionType, List<Transaction>> groupByType(List<Transaction> txs);
    Map<LocalDate, List<Transaction>> groupByDate(List<Transaction> txs);
    Map<Integer, List<Transaction>> groupByAccount(List<Transaction> txs);

    // Aggregation
    double totalAmount(List<Transaction> txs);
    double averageAmount(List<Transaction> txs);

    // Anomaly detection
    List<Transaction> detectSuspiciousTransactions(
            List<Transaction> txs,
            double highAmountThreshold,
            Set<String> usualLocationsOrCountries,
            int burstCountThreshold
    );
}