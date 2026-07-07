
package model;

import java.io.Serializable;

public class Client extends Person implements Serializable{
    public Client(int id, String firstName, String lastName, String phone, String email) {
        super(id, firstName, lastName, phone, email);
    }
}
