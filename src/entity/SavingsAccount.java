package entity;
import java.util.Set;

public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(int id, String number, double balance, int clientId, double interestRate) {
        super(id, number, balance, clientId);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}