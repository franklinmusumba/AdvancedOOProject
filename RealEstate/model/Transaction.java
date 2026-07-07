package model;

import java.io.Serializable;
import java.sql.Date;

public class Transaction implements Serializable{
    private final int transactionId;
    private final Property property;
    private final Client client;
    private final Date transactionDate;
    private final double amount;
    private final TransactionType transactionType; 

    public Transaction(int transactionId, Property property, Client client,
                       Date transactionDate, double amount, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.property = property;
        this.client = client;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public int getTransactionId() { return transactionId; }
    public Property getProperty() { return property; }
    public Client getClient() { return client; }
    public Date getTransactionDate() { return transactionDate; }
    public double getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
}
