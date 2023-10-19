package ServerApplication.Services;

import java.io.Serializable;
import java.rmi.RemoteException;

public class AuthenticationService implements IAuthenticationService, Serializable {
    //TODO: implement password storage (System File, Public File, DBMS)
    //TODO: password verification
    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        return true;
    }
}
