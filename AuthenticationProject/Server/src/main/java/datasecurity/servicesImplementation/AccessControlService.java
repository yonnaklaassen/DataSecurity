package datasecurity.servicesImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datasecurity.Shared.ConsoleColors;
import model.Permission;
import datasecurity.model.User;
import datasecurity.services.IAccessControlService;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class AccessControlService extends UnicastRemoteObject implements IAccessControlService, Serializable {

    private List<User> users;

    public AccessControlService() throws RemoteException {
        super();
        this.users = new ArrayList<>();
        loadAccessControlList();
    }

    public void loadAccessControlList() {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode aclData = objectMapper.readTree(new File("files/AccessControlList.json"));

        // Load users
        JsonNode usersArray = aclData.get("users");
        for (JsonNode userNode : usersArray) {
            String username = userNode.get("username").asText();
            List<String> permissionListStrings = new ArrayList<>();
            for(JsonNode permissions : userNode.get("permissions")) {
                permissionListStrings.add(permissions.asText());
            }

            List<Permission> permissionList = new ArrayList<>();
            for (String perm : permissionListStrings) {
                permissionList.add(convertStringToPermission(perm));
            }

            users.add(new User(username, permissionList));
        }

        System.out.println(ConsoleColors.GREEN + "Access control list is loaded!.");
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    }

    private Permission convertStringToPermission(String permission) {
        for(Enum enumConstant : Permission.class.getEnumConstants()) {
            if(enumConstant.name().equalsIgnoreCase(permission)) {
                return (Permission) enumConstant;
            }
        }
        return null;
    }

    public boolean checkPermission(String username, Permission operation) {
        User user = getUserByUsername(username);
        if (user != null) {
            List<Permission> userPermissions = user.getPermissionList();
            return userPermissions.contains(operation);
        }
        return false; // Handle error if user not found
    }

    public List<Permission> getPermissionsByUser(String username) {
    return Objects.requireNonNull(getUserByUsername(username)).getPermissionList();
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // Handle error if user not found
    }
}
