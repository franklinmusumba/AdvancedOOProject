
package foundation;

public class TransactionType implements TransactionTypeInterface{
    private final int typeId;
    private final String typeName;
    
    public TransactionType(int typeId, String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }
    
    @Override
    public int getTypeId(){
        return typeId;
    }
    
    @Override
    public String getTypeName(){
        return typeName;
    }
}
