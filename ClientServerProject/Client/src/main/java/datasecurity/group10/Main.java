package datasecurity.group10;



import datasecurity.group10.services.IAuthenticationService;
import datasecurity.group10.services.IPrintService;

import java.rmi.Naming;

public class Main {
    public static void main(String[] args) {
        try {
            // Get the reference to the RMI registry on localhost, port 1099
            IAuthenticationService authenticationService = (IAuthenticationService) Naming.lookup("rmi://localhost:1099/authenticationObject");

            if(authenticationService.authenticate("test", "test")) {
                // Get access to print service
                IPrintService printService = (IPrintService) Naming.lookup("rmi://localhost:1099/printerObject");
                System.out.println(printService.getClass().getName());
                printService.print("test.txt","1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
