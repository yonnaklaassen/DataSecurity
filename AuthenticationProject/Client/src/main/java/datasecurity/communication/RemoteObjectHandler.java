package datasecurity.communication;

import datasecurity.services.IAuthenticationService;
import datasecurity.services.IPrintService;
import org.springframework.stereotype.Component;


import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

@Component
public class RemoteObjectHandler {
    Registry registry;

    public RemoteObjectHandler() throws RemoteException {
        System.setProperty("javax.net.ssl.trustStore", "certificate/certificate.pfx");
        System.setProperty("javax.net.ssl.trustStorePassword", "group10");
        RMIClientSocketFactory clientFactory = new SslRMIClientSocketFactory();
         this.registry = LocateRegistry.getRegistry("localhost", 1099, clientFactory);

    }

    public IAuthenticationService getRemoteAuthenticationObject() throws MalformedURLException, NotBoundException, RemoteException {
       IAuthenticationService authenticationService =(IAuthenticationService) registry.lookup("authenticationObject");
        System.out.println(authenticationService.getClass());


        return (IAuthenticationService) registry.lookup("authenticationObject");

    }
    public IPrintService getRemotePrintServiceObject() throws MalformedURLException, NotBoundException, RemoteException {

        return (IPrintService)registry.lookup("printServiceObject");

    }

}
