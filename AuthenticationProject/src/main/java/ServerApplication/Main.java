package ServerApplication;

import Services.PrintService;
import Shared.ConsoleColors;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {

            PrintService remotePrintServiceObject = new PrintService();
            // Create the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the remote object with a name
            registry.rebind("printerObject", remotePrintServiceObject);

            System.out.println(ConsoleColors.GREEN + "Server is ready.");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
