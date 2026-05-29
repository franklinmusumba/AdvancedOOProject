package foundation;

public class Client implements ClientInterface{
    private final int clientId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final String password;
    
    public Client(int clientId, String firstName, String lastName, String phone, String email, String password){
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    
    @Override
    public int getClientId(){
        return clientId;
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
    public String getPhone(){
        return phone;
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
