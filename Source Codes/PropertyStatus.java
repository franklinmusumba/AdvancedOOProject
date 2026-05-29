package foundation;

public class PropertyStatus implements PropertyStatusInterface{
    private final int statusId;
    private final String statusName;
    
    public PropertyStatus(int statusId, String statusName){
        this.statusId = statusId;
        this.statusName = statusName;
    }
    
    @Override
    public int getStatusId(){
        return statusId;
    }
    
    @Override
    public String getStatusName(){
        return statusName;
    }
    
}
