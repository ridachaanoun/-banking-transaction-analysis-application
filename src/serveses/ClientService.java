package serveses;

import entity.Account;
import entity.Client;

import java.util.List;

public interface ClientService {
    // CRUD
    void addClient(Client client) throws Exception;
    void updateClient(Client client) throws Exception;
    void deleteClient(int id) throws Exception;

    Client findClientById(int id) throws Exception;
    List<Client> findClientsByName(String name) throws Exception;
    List<Client> listAllClients() throws Exception;

    // Business logic
    double getTotalBalanceForClient(int clientId) throws Exception;
    Account getMaxBalanceAccountForClient(int clientId) throws Exception;
    Account getMinBalanceAccountForClient(int clientId) throws Exception;
}