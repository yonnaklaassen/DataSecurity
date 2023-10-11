package ClientApplication;

import ServerApplication.Printer;
import Services.PrintService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            // Get the reference to the RMI registry on localhost, port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // Look up the remote object by the name used during binding
            PrintService remoteObject = (PrintService) registry.lookup("printerObject");
            remoteObject.print("test.txt","1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
