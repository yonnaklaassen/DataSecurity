package datasecurity.Persistence;

import java.sql.*;

public class DataBaseConnection {
    Connection connection;

    public DataBaseConnection() {
        this.connection = connection;
    }

    public String[] getHashedPasswordSalt(String username) throws SQLException {
        String hashedPassword="";
        String salt="";

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3333/db?useSSL=false","root","root");
            //Statement s = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE username =? ");
            ps.setString(1, username);

            ResultSet resset = ps.executeQuery();


            while(resset.next()){
                //Code to get hashed password for user username
                hashedPassword = resset.getString("hashedPassword");
                salt = resset.getString("salt");
            }


        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        connection.close();
        System.out.println(salt+hashedPassword);
        String[] result = {salt, hashedPassword};
        return result;
    }



}
