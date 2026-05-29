package foundation;
import java.sql.Date;
public class Transaction implements TransactionInterface{
    private final int transactionId;
    private final PropertyInterface property;
    private final ClientInterface client;
    private final Date transactionDate;
    private final double amount;
    private final TransactionTypeInterface transactionType;
    
    public Transaction(int transactionId, Property property, Client client, Date transactionDate, double amount, TransactionType transactionType){
        this.transactionId = transactionId;
        this.property = property;
        this.client = client;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
    }
    
    @Override
    public int getTransactionId(){
        return transactionId;
    }
   
    
    @Override
    public ClientInterface getClient(){
        return client;
    }
    
    @Override
    public Date getTransactionDate(){
        return transactionDate;
    }
    
    @Override
    public double getAmount(){
        return amount;
    }

    @Override
    public TransactionTypeInterface getTransactionType() {
        return this.transactionType;
    }

    @Override
    public PropertyInterface getProperty() {
       return property;
    }
}
