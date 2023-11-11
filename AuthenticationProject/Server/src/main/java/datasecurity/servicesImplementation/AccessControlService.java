package datasecurity.servicesImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datasecurity.Shared.ConsoleColors;
import datasecurity.model.Role;
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
    private List<Role> roles;

    public AccessControlService() throws RemoteException {
        super();
        this.users = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public void loadAccessControlList() {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode aclData = objectMapper.readTree(new File("files/AccessControlList.json"));

        // Load roles
        JsonNode rolesArray = aclData.get("roles");
        for (JsonNode roleNode : rolesArray) {
            String roleName = roleNode.get("name").asText();
            List<String> permissionListStrings = new ArrayList<>();
            for(JsonNode permissions : roleNode.get("permissions")) {
                permissionListStrings.add(permissions.asText());
            }
            List<Permission> permissionList = new ArrayList<>();
            for (String perm : permissionListStrings) {
                permissionList.add(convertStringToPermission(perm));
            }
            roles.add(new Role(roleName, permissionList));
        }

        // Load users
        JsonNode usersArray = aclData.get("users");
        for (JsonNode userNode : usersArray) {
            String username = userNode.get("username").asText();
            String roleName = userNode.get("role").asText();
            Role userRole = getRoleByName(roleName);
            users.add(new User(username, userRole));
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

    private Role getRoleByName(String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return role;
            }
        }
        return null; // Handle error if role not found
    }

    public boolean checkPermission(String username, Permission operation) {
        User user = getUserByUsername(username);
        if (user != null) {
            List<Permission> userPermissions = user.getRole().getPermissions();
            return userPermissions.contains(operation);
        }
        return false; // Handle error if user not found
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
