package serveses.impl;

import dao.AccountDAO;
import dao.TransactionDAO;
import dao.impl.AccountDAOImpl;
import dao.impl.TransactionDAOImpl;
import entity.Account;
import entity.Transaction;
import entity.TransactionType;
import serveses.TransactionService;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO = new TransactionDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    // CRUD
    @Override
    public void addTransaction(Transaction t) throws Exception {
        transactionDAO.addTransaction(t);
    }

    @Override
    public void updateTransaction(Transaction t) throws Exception {
        transactionDAO.updateTransaction(t);
    }

    @Override
    public void deleteTransaction(int id) throws Exception {
        transactionDAO.deleteTransaction(id);
    }

    @Override
    public Transaction findTransactionById(int id) throws Exception {
        return transactionDAO.findTransactionById(id);
    }

    @Override
    public List<Transaction> listTransactionsByAccount(int accountId) throws Exception {
        return transactionDAO.findTransactionsByAccountId(accountId);
    }

    @Override
public List<Transaction> listTransactionsByClient(int clientId) throws Exception {
    return transactionDAO.findTransactionsByAccountId(clientId)
            .stream()
            .collect(Collectors.toList());
}

    @Override
    public List<Transaction> listAllTransactions() throws Exception {
        return transactionDAO.findAllTransactions();
    }

    // Filters and grouping
    @Override
    public List<Transaction> filterTransactions(
            List<Transaction> source,
            Double minAmount,
            Double maxAmount,
            TransactionType type,
            LocalDate fromDate,
            LocalDate toDate,
            String locationContains
    ) {
        Predicate<Transaction> p = t -> true;
        if (minAmount != null) p = p.and(t -> t.amount() >= minAmount);
        if (maxAmount != null) p = p.and(t -> t.amount() <= maxAmount);
        if (type != null) p = p.and(t -> t.type() == type);
        if (fromDate != null) p = p.and(t -> !t.date().isBefore(fromDate));
        if (toDate != null) p = p.and(t -> !t.date().isAfter(toDate));
        if (locationContains != null && !locationContains.isBlank()) {
            String needle = locationContains.toLowerCase();
            p = p.and(t -> t.location() != null && t.location().toLowerCase().contains(needle));
        }
        return source.stream().filter(p).toList();
    }

    @Override
    public Map<TransactionType, List<Transaction>> groupByType(List<Transaction> txs) {
        return txs.stream().collect(Collectors.groupingBy(Transaction::type));
    }

    @Override
    public Map<LocalDate, List<Transaction>> groupByDate(List<Transaction> txs) {
        return txs.stream().collect(Collectors.groupingBy(Transaction::date));
    }

    @Override
    public Map<Integer, List<Transaction>> groupByAccount(List<Transaction> txs) {
        return txs.stream().collect(Collectors.groupingBy(Transaction::accountId));
    }

    // Aggregation
    @Override
    public double totalAmount(List<Transaction> txs) {
        return txs.stream().mapToDouble(Transaction::amount).sum();
    }

    @Override
    public double averageAmount(List<Transaction> txs) {
        return txs.stream().mapToDouble(Transaction::amount).average().orElse(0.0);
    }

    // Anomaly detection
    @Override
    public List<Transaction> detectSuspiciousTransactions(
            List<Transaction> txs,
            double highAmountThreshold,
            Set<String> usualLocationsOrCountries,
            int burstCountThreshold
    ) {
        List<Transaction> suspicious = new ArrayList<>();

        // High amount rule
        suspicious.addAll(txs.stream()
                .filter(t -> t.amount() >= highAmountThreshold)
                .toList());

        // Unusual location rule
        suspicious.addAll(txs.stream()
                .filter(t -> t.location() != null && !usualLocationsOrCountries.contains(t.location()))
                .toList());

        // Burst frequency rule per account (LocalDate granularity -> same-day bursts)
        Map<Integer, List<Transaction>> byAccount = groupByAccount(txs);
        for (List<Transaction> list : byAccount.values()) {
            Map<LocalDate, Long> byDay = list.stream()
                    .collect(Collectors.groupingBy(Transaction::date, Collectors.counting()));
            byDay.forEach((day, count) -> {
                if (count >= burstCountThreshold) {
                    suspicious.addAll(list.stream().filter(t -> t.date().equals(day)).toList());
                }
            });
        }

        return suspicious.stream().distinct().toList();
    }
}