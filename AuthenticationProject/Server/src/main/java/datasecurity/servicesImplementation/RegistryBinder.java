package datasecurity.servicesImplementation;

import datasecurity.services.IAuthenticationService;

import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;

public class RegistryBinder {

    static Registry registry;

    static {
        try {
            System.setProperty("javax.net.ssl.keyStorePassword", "group10");
            System.setProperty("javax.net.ssl.keyStore", "certificate/certificate.pfx");

            registry = LocateRegistry.createRegistry(1099, null, new SslRMIServerSocketFactory());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static void bindAuthenticationService() throws RemoteException {
        IAuthenticationService remoteAuthenticationServiceObject = new AuthenticationService();
        registry.rebind("authenticationObject", remoteAuthenticationServiceObject);
    }

    public static void bindPrintService(String referenceCookie) throws RemoteException {
        PrintService remotePrintServiceObject = new PrintService(referenceCookie);
        registry.rebind("printServiceObject"+referenceCookie, remotePrintServiceObject);

    }


    public static void unBindPrintService(String referenceCookie) throws NotBoundException, RemoteException {
        registry.unbind("printServiceObject"+referenceCookie);
    }

}
