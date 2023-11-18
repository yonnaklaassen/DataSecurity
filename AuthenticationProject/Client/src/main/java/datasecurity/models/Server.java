package datasecurity.models;

import model.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Server {
    String status;
    List<Permission> permissionList;

public Server(){
    status="stop";

}
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPermissions(List<Permission> permissions) {
    permissionList = permissions;
    }

    public boolean permission(String action) {
    for(Permission perm : permissionList) {
        if(perm.name().equalsIgnoreCase(action)) {
            return true;
        }
    }
    return false;
    }
}
