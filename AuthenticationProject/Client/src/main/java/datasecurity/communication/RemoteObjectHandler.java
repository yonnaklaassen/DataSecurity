package datasecurity.communication;

import datasecurity.ClientSecurity.UsersConfig;
import datasecurity.services.IAccessControlService;
import datasecurity.services.IAuthenticationService;
import datasecurity.services.IPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;


@Component
public class RemoteObjectHandler {
    Registry registry;
    IAuthenticationService authenticationService;
    IPrintService printService ;
    IAccessControlService accessControlService;

    @Autowired
    UsersConfig userDetails;

    public RemoteObjectHandler( ) throws IOException, NotBoundException{
        System.setProperty("javax.net.ssl.trustStore", "certificate/RMI_ClientTrustStore.pfx");
        System.setProperty("javax.net.ssl.trustStorePassword", "group10");
        String host = System.getenv("hostIp");
        RMIClientSocketFactory clientFactory = new SslRMIClientSocketFactory();
        registry = LocateRegistry.getRegistry(host, 1099,clientFactory);
        authenticationService =(IAuthenticationService) registry.lookup("authenticationObject");
    }

    public IAuthenticationService getRemoteAuthenticationObject() {

        return authenticationService;

    }
    public IPrintService getRemotePrintServiceObject() throws NotBoundException, RemoteException {
        printService= (IPrintService)registry.lookup("printServiceObject"+userDetails.get_sessionAuthCookie());

        return printService;

    }

    public IAccessControlService getAccessControlServiceObject() throws NotBoundException, RemoteException {
        accessControlService = (IAccessControlService)registry.lookup("accessControlServiceObject"+userDetails.get_sessionAuthCookie());
        return accessControlService;
    }


  /*  public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {
        System.setProperty("javax.net.ssl.trustStore", "certificate/certificate.pfx");
        System.setProperty("javax.net.ssl.trustStorePassword", "group10");
        RMIClientSocketFactory clientFactory = new SslRMIClientSocketFactory();
        Registry registry = LocateRegistry.getRegistry("localhost", 1099, clientFactory);
        IAuthenticationService authenticationService =(IAuthenticationService) registry.lookup("authenticationObject");
        String test = authenticationService.authenticate("alice23","helloworld");
        System.out.println(test);
        IPrintService printService =  (IPrintService) registry.lookup("printServiceObject"+test);
        printService.start();


    }
*/
}
