package dao.impl;

import dao.ClientDAO;
import entity.Client;
import resources.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO client(name, email) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.name());
            ps.setString(2, client.email());
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("addClient did not insert exactly one row");
        } catch (SQLException e) {
            throw new RuntimeException("addClient failed", e);
        }
    }
    @Override
    public void updateClient(Client client) {
        String sql = "UPDATE client SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.name());
            ps.setString(2, client.email());
            ps.setInt(3, client.id());
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("updateClient affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("updateClient failed", e);
        }
    }

    @Override
    public void deleteClient(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("deleteClient affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("deleteClient failed", e);
        }
    }

    @Override
    public Client findClientById(int id) {
        String sql = "SELECT id, name, email FROM client WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("findClientById failed", e);
        }
    }

    @Override
    public List<Client> findClientsByName(String name) {
        String sql = "SELECT id, name, email FROM client WHERE LOWER(name) LIKE LOWER(?) ORDER BY id";
        List<Client> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findClientsByName failed", e);
        }
    }

    @Override
    public List<Client> findAllClients() {
        String sql = "SELECT id, name, email FROM client ORDER BY id";
        List<Client> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findAllClients failed", e);
        }
    }
}