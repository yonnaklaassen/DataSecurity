package datasecurity.communication;



import datasecurity.services.IAuthenticationService;
import datasecurity.services.IPrintService;


import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

public class Main {
    public static void main(String[] args) {
        try {
            // Get the reference to the RMI registry on localhost, port 1099


            System.setProperty("javax.net.ssl.trustStore", "certificate/certificate.pfx");
            System.setProperty("javax.net.ssl.trustStorePassword", "group10");

            RMIClientSocketFactory clientFactory = new SslRMIClientSocketFactory();
            Registry registry = LocateRegistry.getRegistry("localhost", 1099, clientFactory);

            IAuthenticationService authenticationService = (IAuthenticationService) registry.lookup("authenticationObject");
            System.out.println(authenticationService.getClass().getName());

            if(authenticationService.authenticate("alice23", "helloworld")) {
                // Get access to print service
                IPrintService printService = (IPrintService) registry.lookup("printServiceObject");
                System.out.println(printService.getClass().getName());
                printService.print("test.txt","1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
