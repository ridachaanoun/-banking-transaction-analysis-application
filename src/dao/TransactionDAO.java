package dao;
import entity.Transaction;
import java.util.List;

public interface TransactionDAO {
    void addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransaction(int id);
    Transaction findTransactionById(int id);
    List<Transaction> findTransactionsByAccountId(int accountId);
    List<Transaction> findAllTransactions();
}