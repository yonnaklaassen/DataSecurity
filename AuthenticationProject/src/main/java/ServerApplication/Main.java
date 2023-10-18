package ServerApplication;

import ServerApplication.Services.PrintService;
import Shared.ConsoleColors;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, AlreadyBoundException {
       // try {
            SecurityChecker securityChecker= new SecurityChecker();
            // ServerSocket serverSocket= new ServerSocket(1099) ;
            //serverSocket.setSoTimeout(100000000);
            PrintService remotePrintServiceObject = new PrintService();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("printerObject",remotePrintServiceObject);
            System.out.println("Server is started");
            while (true){
              //  Socket inputSocket  = serverSocket.accept();
              //  InputStream inputStream = inputSocket.getInputStream();

              //  System.out.println("continues"+inputStream.read());
                Thread.sleep(1000);

            }


/*
            // Read data from the input stream
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
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

*/

    }
}
