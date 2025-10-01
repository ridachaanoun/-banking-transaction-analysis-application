package serveses.impl;

import dao.AccountDAO;
import dao.ClientDAO;
import dao.impl.AccountDAOImpl;
import dao.impl.ClientDAOImpl;
import entity.Account;
import entity.Client;
import serveses.ClientService;

import java.util.Comparator;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final ClientDAO clientDAO = new ClientDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    // CRUD
    @Override
    public void addClient(Client client) throws Exception {
        clientDAO.addClient(client);
    }

    @Override
    public void updateClient(Client client) throws Exception {
        clientDAO.updateClient(client);
    }

    @Override
    public void deleteClient(int id) throws Exception {
        clientDAO.deleteClient(id);
    }

    @Override
    public Client findClientById(int id) throws Exception {
        return clientDAO.findClientById(id);
    }

    @Override
    public List<Client> findClientsByName(String name) throws Exception {
        return clientDAO.findClientsByName(name);
    }

    @Override
    public List<Client> listAllClients() throws Exception {
        return clientDAO.findAllClients();
    }

    // Business logic
    @Override
    public double getTotalBalanceForClient(int clientId) throws Exception {
        return accountDAO.findAccountsByClientId(clientId)
                .stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public Account getMaxBalanceAccountForClient(int clientId) throws Exception {
        return accountDAO.findAccountsByClientId(clientId)
                .stream()
                .max(Comparator.comparingDouble(Account::getBalance))
                .orElse(null);
    }

    @Override
    public Account getMinBalanceAccountForClient(int clientId) throws Exception {
        return accountDAO.findAccountsByClientId(clientId)
                .stream()
                .min(Comparator.comparingDouble(Account::getBalance))
                .orElse(null);
    }
}