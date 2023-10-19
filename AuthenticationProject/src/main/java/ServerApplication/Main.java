package ServerApplication;

import ServerApplication.Services.AuthenticationService;
import ServerApplication.Services.PrintService;
import Shared.ConsoleColors;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {

            // Create the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println(ConsoleColors.GREEN + "Server is ready.");

            AuthenticationService remoteAuthenticationServiceObject = new AuthenticationService();
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
