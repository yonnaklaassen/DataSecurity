package datasecurity.unittest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import datasecurity.Shared.ConsoleColors;
import datasecurity.exeptions.UnauthorizedException;
import datasecurity.model.User;
import datasecurity.servicesImplementation.AccessControlService;
import datasecurity.servicesImplementation.AuthenticationService;
import datasecurity.servicesImplementation.PrintService;
import org.junit.jupiter.api.Test;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthorizationTest {



   @Test
    void bobChangePermission() throws Exception {
        // Create new Authentication service instance
        AuthenticationService authenticationService= new AuthenticationService();
        // Get authentication cookie for user bob
        String bobAuthenticationCookie=authenticationService.authenticate("bob23","bobhelloworld");

        PrintService bobPrintService = new PrintService(bobAuthenticationCookie);
        //Ensure that bob can start the server
        bobPrintService.start();
        // Assert he can not execute print(), queue(), top_queue()
        assertThrows(UnauthorizedException.class,()->{bobPrintService.print("testFile","Printer1");});
        assertThrows(UnauthorizedException.class,()->{bobPrintService.queue("Printer1");});
        assertThrows(UnauthorizedException.class,()->{bobPrintService.topQueue("Printer1",1);});
        // get the policy file
        String policyFilePath = "accessControlForTest/AccessControlListForTest.json";
        File policyFile = new File(policyFilePath);
        ObjectMapper obMapper = new ObjectMapper();
        JsonNode policyData= obMapper.readTree(policyFile);
        ArrayNode bobPermissions = null;
        for (Iterator<JsonNode> iterator = policyData.get("users").elements(); iterator.hasNext(); ) {
            JsonNode element = iterator.next();
            if (element.get("username").asText().equals("bob23")) {
                // get the permissions assigned to bob
                bobPermissions = (ArrayNode)element.get("permissions");
                // remove bob from the policy
                iterator.remove();
                System.out.println(ConsoleColors.RED+"User bob23 is removed");
                break;
            }
        }
        // Overwrite the policy file without bob
        obMapper.writeValue(policyFile, policyData);
        // Assert that bob can not stop the server anymore
        assertThrows(UnauthorizedException.class, bobPrintService::stop);
        // Create a node with the new employee george23
        ObjectNode georgeNode = obMapper.createObjectNode();
        georgeNode.put("username","george23");
        // Assign bob permissions to george
        georgeNode.put("permissions",bobPermissions);
        // Overwrite the policy file with george info
        ArrayNode usersNode= obMapper.createArrayNode().add(georgeNode);
        usersNode.add(georgeNode.asText());
        ObjectNode newFileNode =  obMapper.createObjectNode();
        newFileNode.put("users",usersNode);
        System.out.println(ConsoleColors.GREEN+"New policy data "+newFileNode);
        obMapper.writeValue(policyFile,newFileNode);
        // Authenticate george
        String georgeAuthenticationCookie=authenticationService.authenticate("george23","georgehelloworld");
        PrintService georgePrintService = new PrintService(georgeAuthenticationCookie);
        //Ensure that george can stop the server
        georgePrintService.stop();

    }
}
