package entity;

public class Account {

    protected int id;
    protected String number;
    protected double balance;
    protected int clientId;

    public Account(int id, String number, double balance, int clientId) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.clientId = clientId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    @Override
    public String toString() {
        return "Account [id=" + id + ", balance=" + balance + ", number=" + number + "]";
    }
}