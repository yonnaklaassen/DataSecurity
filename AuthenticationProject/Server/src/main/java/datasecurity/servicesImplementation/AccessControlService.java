package datasecurity.servicesImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datasecurity.Shared.ConsoleColors;
import model.Permission;
import datasecurity.model.User;
import datasecurity.services.IAccessControlService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class AccessControlService extends UnicastRemoteObject implements IAccessControlService, Serializable {

    private User currentUser;

    public AccessControlService() throws RemoteException {
        super();
    }
git
    public List<Permission> loadAccessControlPermissions(String referenceCookie) {
    try {
        String currentUsername = Session.getSession(referenceCookie).username;
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath1 = "/accessControl/AccessControlList.json";
        String filePath2 = "Authentication-Project-docker-ready/Authentication-Project/accessControl/AccessControlList.json";

        File file1 = new File(filePath1);
        File file2 = new File(filePath2);

        JsonNode aclData;
        if (file1.exists()) {
            aclData = objectMapper.readTree(file1);
        } else if (file2.exists()) {
            aclData = objectMapper.readTree(file2);
        } else {
            throw new FileNotFoundException("AccessControlList.json not found in either path: " + filePath1 + " or " + filePath2);
        }

        // Load user
        JsonNode usersArray = aclData.get("users");
        for (JsonNode userNode : usersArray) {
            String username = userNode.get("username").asText();

            List<String> permissionListStrings = new ArrayList<>();
            if(username.equals(currentUsername)) {
                for(JsonNode permissions : userNode.get("permissions")) {
                    permissionListStrings.add(permissions.asText());
                }

                List<Permission> permissionList = new ArrayList<>();
                for (String perm : permissionListStrings) {
                    permissionList.add(convertStringToPermission(perm));
                }
                currentUser = new User(currentUsername, permissionList);
                System.out.println(ConsoleColors.GREEN + "Access control permissions for " + currentUsername + " is loaded!");
                System.out.println(ConsoleColors.ORANGE + permissionList);
                return permissionList;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    return new ArrayList<>();
    }

    private Permission convertStringToPermission(String permission) {
        for(Enum enumConstant : Permission.class.getEnumConstants()) {
            if(enumConstant.name().equalsIgnoreCase(permission)) {
                return (Permission) enumConstant;
            }
        }
        return null;
    }

    public List<Permission> getUserPermissionList() {
        return currentUser.getPermissionList();
    }
}
