package dao;
import entity.Account;
import java.util.List;

public interface AccountDAO {
    void addAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(int id);
    Account findAccountById(int id);
    List<Account> findAccountsByClientId(int clientId);
    List<Account> findAllAccounts();
}