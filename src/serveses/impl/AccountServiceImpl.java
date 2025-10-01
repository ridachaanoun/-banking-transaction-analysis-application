package serveses.impl;

import dao.AccountDAO;
import dao.impl.AccountDAOImpl;
import entity.Account;
import serveses.AccountService;

import java.util.Comparator;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO = new AccountDAOImpl();

    // CRUD
    @Override
    public void createAccount(Account account) throws Exception {
        accountDAO.addAccount(account);
    }

    @Override
    public void updateAccount(Account account) throws Exception {
        accountDAO.updateAccount(account);
    }

    @Override
    public void deleteAccount(int id) throws Exception {
        accountDAO.deleteAccount(id);
    }

    @Override
    public Account findAccountById(int id) throws Exception {
        return accountDAO.findAccountById(id);
    }

    @Override
    public List<Account> findAccountsByClientId(int clientId) throws Exception {
        return accountDAO.findAccountsByClientId(clientId);
    }

    @Override
    public List<Account> listAllAccounts() throws Exception {
        return accountDAO.findAllAccounts();
    }

    // Queries
    @Override
    public Account findAccountWithMaxBalance() throws Exception {
        return accountDAO.findAllAccounts()
                .stream()
                .max(Comparator.comparingDouble(Account::getBalance))
                .orElse(null);
    }

    @Override
    public Account findAccountWithMinBalance() throws Exception {
        return accountDAO.findAllAccounts()
                .stream()
                .min(Comparator.comparingDouble(Account::getBalance))
                .orElse(null);
    }
}