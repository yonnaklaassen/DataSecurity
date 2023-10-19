package ServerApplication.Services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthenticationService extends Serializable, Remote {

    boolean authenticate(String username, String password) throws RemoteException;
}
