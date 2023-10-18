package ServerApplication;

import java.net.Socket;
import java.net.SocketPermission;
import java.security.Permission;

public class SecurityChecker extends SecurityManager{

    public SecurityChecker() {

        System.setProperty("java.security.policy", "src/main/resources/permissions.policy");
        System.setSecurityManager(this);
    }

    @Override
public void checkPermission(Permission per){
        try {


        if (per instanceof SocketPermission) {
            SocketPermission socketPerm = (SocketPermission) per;
            String actions = socketPerm.getActions();
            String host = socketPerm.getName();
            System.out.println("actions :"+actions+"  host:"+host);

            // Allow both connect and accept operations involving the local host
            if (actions.equals("accept,resolve")) {
                System.out.println("fffffffffffffffff");


                return; // Allow the operation
            }

        }
        // You can add more checks for other types of permissions.policy if needed.
    }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }}








