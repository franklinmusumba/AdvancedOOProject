
package foundation;

public class PropertyType implements PropertyTypeInterface{
    private final int typeId;
    private final String typeName;
    
    public PropertyType(int typeId, String typeName){
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
