package datasecurity.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String toString() {
        return "Username: " + getUsername() + "\nRole: " + getRole().getName();
    }
}
