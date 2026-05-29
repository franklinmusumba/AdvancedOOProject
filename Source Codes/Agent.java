
package foundation;

public class Agent implements AgentInterface{
    private final int agentId;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;
    private final String password;
    
    public Agent(int agentId, String firstName, String lastName, String phoneNumber, String email,String password){
        this.agentId = agentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
    
    @Override
    public int getAgentId(){
        return agentId;
    }
    @Override
    public String getFirstName(){
        return firstName;
    }
    
    @Override
    public String getLastName(){
        return lastName;
    }
    
    @Override
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    @Override
    public String getEmail(){
        return email;
    }
    
    @Override
    public String getPassword(){
        return password;
    }
}
