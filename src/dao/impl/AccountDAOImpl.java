package dao.impl;

import dao.AccountDAO;
import entity.Account;
import entity.CurrentAccount;
import entity.SavingsAccount;
import resources.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO account(number, balance, client_id, account_type, overdraft_limit, interest_rate) " +
                     "VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getNumber());
            ps.setDouble(2, account.getBalance());
            ps.setInt(3, account.getClientId());

            if (account instanceof CurrentAccount ca) {
                ps.setString(4, "CURRENT");
                ps.setObject(5, ca.getOverdraftLimit(), Types.DOUBLE);
                ps.setNull(6, Types.DOUBLE);
            } else if (account instanceof SavingsAccount sa) {
                ps.setString(4, "SAVINGS");
                ps.setNull(5, Types.DOUBLE);
                ps.setObject(6, sa.getInterestRate(), Types.DOUBLE);
            } else {
                throw new IllegalArgumentException("Unknown Account subtype");
            }

            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("addAccount did not insert exactly one row");
        } catch (SQLException e) {
            throw new RuntimeException("addAccount failed", e);
        }
    }

    @Override
    public void updateAccount(Account account) {
        String sql = "UPDATE account SET number = ?, balance = ?, client_id = ?, account_type = ?, overdraft_limit = ?, interest_rate = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getNumber());
            ps.setDouble(2, account.getBalance());
            ps.setInt(3, account.getClientId());

            if (account instanceof CurrentAccount ca) {
                ps.setString(4, "CURRENT");
                ps.setObject(5, ca.getOverdraftLimit(), Types.DOUBLE);
                ps.setNull(6, Types.DOUBLE);
            } else if (account instanceof SavingsAccount sa) {
                ps.setString(4, "SAVINGS");
                ps.setNull(5, Types.DOUBLE);
                ps.setObject(6, sa.getInterestRate(), Types.DOUBLE);
            } else {
                throw new IllegalArgumentException("Unknown Account subtype");
            }

            ps.setInt(7, account.getId());
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("updateAccount affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("updateAccount failed", e);
        }
    }

    @Override
    public void deleteAccount(int id) {
        String sql = "DELETE FROM account WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("deleteAccount affected rows: " + affected);
        } catch (SQLException e) {
            throw new RuntimeException("deleteAccount failed", e);
        }
    }

    @Override
    public Account findAccountById(int id) {
        String sql = "SELECT id, number, balance, client_id, account_type, overdraft_limit, interest_rate " +
                     "FROM account WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("findAccountById failed", e);
        }
    }

    @Override
    public List<Account> findAccountsByClientId(int clientId) {
        String sql = "SELECT id, number, balance, client_id, account_type, overdraft_limit, interest_rate " +
                     "FROM account WHERE client_id = ? ORDER BY id";
        List<Account> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findAccountsByClientId failed", e);
        }
    }

    @Override
    public List<Account> findAllAccounts() {
        String sql = "SELECT id, number, balance, client_id, account_type, overdraft_limit, interest_rate FROM account ORDER BY id";
        List<Account> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("findAllAccounts failed", e);
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String number = rs.getString("number");
        double balance = rs.getDouble("balance");
        int clientId = rs.getInt("client_id");
        String type = rs.getString("account_type");

        if ("CURRENT".equalsIgnoreCase(type)) {
            Double overdraft = (Double) rs.getObject("overdraft_limit");
            return new CurrentAccount(id, number, balance, clientId, overdraft != null ? overdraft : 0.0);
        } else if ("SAVINGS".equalsIgnoreCase(type)) {
            Double rate = (Double) rs.getObject("interest_rate");
            return new SavingsAccount(id, number, balance, clientId, rate != null ? rate : 0.0);
        } else {
            throw new IllegalStateException("Unknown account_type: " + type);
        }
    }
}