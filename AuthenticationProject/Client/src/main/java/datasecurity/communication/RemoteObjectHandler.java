package datasecurity.communication;

import datasecurity.ClientApplication;
import datasecurity.ClientSecurity.UsersConfig;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Component
public class RemoteObjectHandler {
    Registry registry;
    IAuthenticationService authenticationService;
    IPrintService printService ;

    @Autowired
    UsersConfig userDetails;

    public RemoteObjectHandler( ) throws IOException, NotBoundException {

        System.setProperty("javax.net.ssl.trustStore", "certificate/RMI_ClientTrustStore.pfx");
        System.setProperty("javax.net.ssl.trustStorePassword", "group10");
        String host = System.getenv("hostIp");
        RMIClientSocketFactory clientFactory = new SslRMIClientSocketFactory();
        registry = LocateRegistry.getRegistry(host, 1099,clientFactory);
        authenticationService =(IAuthenticationService) registry.lookup("authenticationObject");
    }

    public IAuthenticationService getRemoteAuthenticationObject() throws MalformedURLException, NotBoundException, RemoteException {

        return authenticationService;

    }
    public IPrintService getRemotePrintServiceObject() throws MalformedURLException, NotBoundException, RemoteException {
        printService= (IPrintService)registry.lookup("printServiceObject"+userDetails.get_sessionAuthCookie());

        return printService;

    }



}
