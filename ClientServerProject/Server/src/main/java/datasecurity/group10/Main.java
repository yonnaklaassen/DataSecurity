package datasecurity.group10;


import datasecurity.group10.services.AuthenticationService;
import datasecurity.group10.services.IAuthenticationService;
import datasecurity.group10.services.PrintService;
import datasecurity.group10.Shared.ConsoleColors;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {

            // Create the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println(ConsoleColors.GREEN + "Server is ready.");

            IAuthenticationService remoteAuthenticationServiceObject = new AuthenticationService();
            registry.rebind("authenticationObject", remoteAuthenticationServiceObject);

            PrintService remotePrintServiceObject = new PrintService();
            registry.rebind("printerObject", remotePrintServiceObject);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
