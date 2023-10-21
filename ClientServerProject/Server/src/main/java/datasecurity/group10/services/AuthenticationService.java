package datasecurity.group10.services;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationService extends UnicastRemoteObject implements IAuthenticationService, Serializable {
    public AuthenticationService() throws RemoteException {
        super();
    }

    //TODO: implement password storage (System File, Public File, DBMS)
    //TODO: password verification
    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        return true;
    }
}
