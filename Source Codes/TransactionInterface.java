package foundation;

import java.sql.Date;

public interface TransactionInterface {
    public int getTransactionId();
    public PropertyInterface getProperty();
    public ClientInterface getClient();
    public Date getTransactionDate();
    public double getAmount();
    public TransactionTypeInterface getTransactionType();
}
