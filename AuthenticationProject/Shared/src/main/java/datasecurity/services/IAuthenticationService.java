package datasecurity.services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IAuthenticationService extends Serializable, Remote {

    boolean authenticate(String username, String password) throws RemoteException, SQLException;
}
