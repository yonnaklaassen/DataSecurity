package datasecurity;


import datasecurity.Shared.ConsoleColors;
import datasecurity.services.AuthenticationService;
import datasecurity.services.IAuthenticationService;
import datasecurity.services.PrintService;

import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            System.setProperty("javax.net.ssl.keyStore", "certificate/certificate.pfx");
            System.setProperty("javax.net.ssl.keyStorePassword", "group10");

            RMIServerSocketFactory serverFactory = new SslRMIServerSocketFactory();
            Registry registry = LocateRegistry.createRegistry(1099, null, serverFactory);

            System.out.println(ConsoleColors.GREEN + "Server is ready.");

            IAuthenticationService remoteAuthenticationServiceObject = new AuthenticationService();
            registry.rebind("authenticationObject", remoteAuthenticationServiceObject);

            PrintService remotePrintServiceObject = new PrintService();
            registry.rebind("printServiceObject", remotePrintServiceObject);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
