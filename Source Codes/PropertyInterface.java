
package foundation;

public interface PropertyInterface {
   public int getPropertyId();
   public String getAddress();
   public String getCity();
   public double getPrice();
   public AgentInterface getManagingAgent();
   public PropertyTypeInterface getPropertyType();
   public PropertyStatusInterface getPropertyStatus();
}
