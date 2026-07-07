package model;

import java.io.Serializable;

public class Agent extends Person implements Serializable{
    private final String password;
    
    public Agent(int agentId, String firstName, String lastName, String phoneNumber, String email,String password){
        super(agentId, firstName, lastName, phoneNumber, email);
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
}
