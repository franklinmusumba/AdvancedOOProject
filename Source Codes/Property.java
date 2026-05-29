
package foundation;

public class Property implements PropertyInterface{
    private final int propertyId;
    private final String address;
    private final String city;
    private final double price;
    private final Agent managingAgent;
    private final PropertyTypeInterface propertyType;
    private final PropertyStatusInterface propertyStatus;
    
    
    public Property(int propertyId, String address, String city, double price, Agent managingAgent, PropertyType propertyType, PropertyStatus propertyStatus){
        this.propertyId = propertyId;
        this.address =address;
        this.city = city;
        this.price = price;
        this.managingAgent = managingAgent;
        this.propertyType = propertyType;
        this.propertyStatus = propertyStatus;
    }
    
    @Override
    public int getPropertyId(){
        return this.propertyId;
    }
    
    @Override
    public String getAddress(){
        return this.address;
    }
    
    @Override
    public String getCity(){
        return this.city;
    }
    
    @Override
    public double getPrice(){
        return this.price;
    }
    
    @Override
    public Agent getManagingAgent(){
        return this.managingAgent;
    }
    
    @Override
    public PropertyTypeInterface getPropertyType(){
        return this.propertyType;
    }
    
    @Override
    public PropertyStatusInterface getPropertyStatus(){
        return this.propertyStatus;
    }
}
