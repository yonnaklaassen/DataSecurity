package datasecurity.Persistence;

import java.sql.*;
import java.util.Enumeration;

public class DataBaseConnection {
    Connection connection;

    public DataBaseConnection() throws SQLException {
// To other group members: If you do not want to add the certificate to you machine trust certificate, you can use the following two lines
// That will ensure the certificate is trusted at least by the server JVM app
         //System.setProperty("javax.net.ssl.trustStore", "certificate/certificate.pfx");
         //System.setProperty("javax.net.ssl.trustStorePassword", "group10");
         //System.setProperty("javax.net.debug", "ssl");

    }

    public static void main(String[] args) throws SQLException {
        //System.setProperty("javax.net.debug","ssl");

        System.out.println(new DataBaseConnection().getHashedPasswordSalt("alice23"));
    }


    public String[] getHashedPasswordSalt(String username) throws SQLException {
        String hashedPassword="";
        String salt="";

        try {

            System.setProperty("javax.net.ssl.trustStore", "files/dbcertificate/certificate.pfx");
            System.setProperty("javax.net.ssl.trustStorePassword", "group10");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?useSSL=true","root","root");

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE username =? ");
            ps.setString(1, username);

            ResultSet resset = ps.executeQuery();
            System.out.println("Certificate Name: " + connection.getClientInfo());


            while(resset.next()){
                //Code to get hashed password for user username
                hashedPassword = resset.getString("hashedPassword");
                salt = resset.getString("salt");
            }


        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        connection.endRequest();

        System.out.println(salt+hashedPassword);
        String[] result = {salt, hashedPassword};
        return result;
    }



}
