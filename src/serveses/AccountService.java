package serveses;

import entity.Account;
import java.util.List;

public interface AccountService {
    // CRUD
    void createAccount(Account account) throws Exception;
    void updateAccount(Account account) throws Exception;
    void deleteAccount(int id) throws Exception;

    Account findAccountById(int id) throws Exception;
    List<Account> findAccountsByClientId(int clientId) throws Exception;
    List<Account> listAllAccounts() throws Exception;

    // Queries
    Account findAccountWithMaxBalance() throws Exception;
    Account findAccountWithMinBalance() throws Exception;
}