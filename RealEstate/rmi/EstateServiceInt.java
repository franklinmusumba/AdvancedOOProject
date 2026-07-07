package rmi;

import model.Agent;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.List;
import model.Client;
import model.Property;
import model.Transaction;
public interface EstateServiceInt extends Remote{
   public Agent authenticate(String email, String password) throws RemoteException;
   public List<Client> getAllClients()throws RemoteException;
   public Client findClientById(int id) throws RemoteException;
   public List<Property> getAllProperties() throws RemoteException;
   public List<Property> findPropertiesByCity(String city) throws RemoteException;
   public Property findPropertyById(int id) throws RemoteException;
   public List<Transaction> getTransactionsByProperty(int propertyId) throws RemoteException;
   public int insertAgent(Agent agent) throws RemoteException;
   public int insertClient(Client client) throws RemoteException;
   public int insertProperty(Property property) throws RemoteException;
   public int insertTransaction(Transaction transaction) throws RemoteException;
   public boolean updateClient(Client client) throws RemoteException;
   public boolean updateProperty(Property property) throws RemoteException;
   public boolean deleteClient(int clientId) throws RemoteException;
   public boolean deleteProperty(int propertyId) throws RemoteException;
   public Agent findAgentById(int id) throws RemoteException;
}
