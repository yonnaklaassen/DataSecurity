package datasecurity.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String username;
    private List<Permission> permissionList;

    public User(String username, List<Permission> permissions) {
        this.username = username;
        this.permissionList = permissions;
    }

    public String getUsername() {
        return username;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public String toString() {
        return "Username: " + getUsername() + "\nPermissions: " + getPermissionList();
    }
}
