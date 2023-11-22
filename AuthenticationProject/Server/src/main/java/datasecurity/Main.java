package datasecurity;


import datasecurity.Shared.ConsoleColors;
import datasecurity.services.IAccessControlService;
import datasecurity.servicesImplementation.AccessControlService;
import datasecurity.servicesImplementation.RegistryBinder;
import java.io.IOException;

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
