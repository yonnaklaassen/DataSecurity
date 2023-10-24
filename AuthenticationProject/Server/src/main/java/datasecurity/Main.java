package datasecurity;


import datasecurity.Shared.ConsoleColors;
import datasecurity.servicesImplementation.AuthenticationService;
import datasecurity.services.IAuthenticationService;
import datasecurity.servicesImplementation.PrintService;
import datasecurity.servicesImplementation.RegistryBinder;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            RegistryBinder registryBinder = new RegistryBinder();
            System.out.println(ConsoleColors.GREEN + "Server is ready.");
            registryBinder.bindAuthenticationService();




            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
