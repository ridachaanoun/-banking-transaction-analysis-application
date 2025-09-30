package dao.impl;

import dao.TransactionDAO;
import entity.Transaction;
import entity.TransactionType;
import resources.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void addTransaction(Transaction t) {
        String sql = "INSERT INTO transaction(date, amount, type, location, account_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(t.date()));
            ps.setDouble(2, t.amount());
            ps.setString(3, t.type().name());
            ps.setString(4, t.location());
            ps.setInt(5, t.accountId());

            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("addTransaction did not insert exactly one row");
        } catch (SQLException e) {
            throw new RuntimeException("addTransaction failed", e);
        }
    }

    @Override
    public void updateTransaction(Transaction t) {
        String sql = "UPDATE transaction SET date = ?, amount = ?, type = ?, location = ?, account_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(t.date()));
            ps.setDouble(2, t.amount());
            ps.setString(3, t.type().name());
            ps.setString(4, t.location());
            ps.setInt(5, t.accountId());
            ps.setInt(6, t.id());

            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("updateTransaction affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("updateTransaction failed", e);
        }
    }

    @Override
    public void deleteTransaction(int id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("deleteTransaction affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("deleteTransaction failed", e);
        }
    }

    @Override
    public Transaction findTransactionById(int id) {
        String sql = "SELECT id, date, amount, type, location, account_id FROM transaction WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("findTransactionById failed", e);
        }
    }

    @Override
    public List<Transaction> findTransactionsByAccountId(int accountId) {
        String sql = "SELECT id, date, amount, type, location, account_id FROM transaction " +
                     "WHERE account_id = ? ORDER BY date DESC, id DESC";
        List<Transaction> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findTransactionsByAccountId failed", e);
        }
    }

    @Override
    public List<Transaction> findAllTransactions() {
        String sql = "SELECT id, date, amount, type, location, account_id FROM transaction ORDER BY id DESC";
        List<Transaction> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findAllTransactions failed", e);
        }
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getDate("date").toLocalDate(),
                rs.getDouble("amount"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getString("location"),
                rs.getInt("account_id")
        );
    }
}