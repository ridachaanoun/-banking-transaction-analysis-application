package entity;
import java.time.LocalDate;

public record Transaction(
    int id,
    LocalDate date,
    double amount,
    TransactionType type,
    String location,
    int accountId
) {}