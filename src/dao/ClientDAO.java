package dao;
import java.util.List;
import entity.Client;
public interface ClientDAO {
    void addClient(Client client);
    void updateClient(Client client);
    void deleteClient(int id);
    Client findClientById(int id);
    List<Client> findClientsByName(String name);
    List<Client> findAllClients();
}