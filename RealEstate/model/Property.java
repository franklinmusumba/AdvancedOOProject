
package model;

import java.io.Serializable;

public class Property implements Serializable{
   private final int propertyId;
    private final String address;
    private final String city;
    private final double price;
    private final Agent managingAgent;
    private final PropertyType type;
    private final PropertyStatus status;

    public Property(int propertyId, String address, String city, double price,
                    Agent managingAgent, PropertyType type, PropertyStatus status) {
        this.propertyId = propertyId;
        this.address = address;
        this.city = city;
        this.price = price;
        this.managingAgent = managingAgent;
        this.type = type;
        this.status = status;
    }

    public int getPropertyId() { return propertyId; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public double getPrice() { return price; }
    public Agent getManagingAgent() { return managingAgent; }
    public PropertyType getType() { return type; }
    public PropertyStatus getStatus() { return status; }

    @Override
    public String toString() {
        return address + ", " + city;
    } 
}
