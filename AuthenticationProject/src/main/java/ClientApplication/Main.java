package ClientApplication;

import ServerApplication.Services.IPrintService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            // Get the reference to the RMI registry on localhost, port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // Look up the remote object by the name used during binding
            IPrintService printService = (IPrintService) registry.lookup("printerObject");
            printService.print("test.txt","1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
