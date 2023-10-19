package ClientApplication;

import ServerApplication.Services.IAuthenticationService;
import ServerApplication.Services.IPrintService;
import Shared.ConsoleColors;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    //TODO: implement secure password transport (SSL, HASH, SALT, ...)
    public static void main(String[] args) {
        try {
            // Get the reference to the RMI registry on localhost, port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            System.out.println(ConsoleColors.GREEN + "Client is ready.");

            IAuthenticationService authenticationService = (IAuthenticationService) registry.lookup("authenticationObject");

            if(authenticationService.authenticate("test", "test")) {
                // Get access to print service
                IPrintService printService = (IPrintService) registry.lookup("printerObject");
                printService.print("test.txt","1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
