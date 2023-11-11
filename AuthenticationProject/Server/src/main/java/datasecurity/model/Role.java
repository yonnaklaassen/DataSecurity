package datasecurity.model;

import model.Permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Role implements Serializable {
    private String name;
    private List<Permission> permissions;

    public Role(String name, List<Permission> permissionList) {
        this.name = name;
        this.permissions = permissionList;
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public String toString() {
        return "Role name: " + getName() + "\nPermissions: " + getPermissions().toString();
    }
}
