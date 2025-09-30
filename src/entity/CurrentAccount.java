package entity;

public final class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(int id, String number, double balance, int clientId, double overdraftLimit) {
        super(id, number, balance, clientId);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}