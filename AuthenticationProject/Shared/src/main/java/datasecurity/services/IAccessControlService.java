package datasecurity.services;

import datasecurity.model.Permission;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAccessControlService extends Serializable, Remote {
     List<Permission> loadAccessControlPermissions(String referenceCookie) throws RemoteException;
}
