
package foundation;
import java.sql.*;
public class DBConnection {
    public Connection con = getConnection();
    
    public Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded Successfully!");
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Driver loading failed "+ cnfe.getMessage());
        }
        
        try{
            String dbname = "realestatedb";
            String dbuser = "postgres";
            String dbpass = "postgres";
            String url = "jdbc:postgresql://localhost:5432/"+dbname;
            
            conn = DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("Driver connected successfully!");
        }catch(SQLException sqle){
            System.out.println("Driver connection failed "+sqle.getMessage());
        }
        return conn;
    }
}
