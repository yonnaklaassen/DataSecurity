package datasecurity.models;

import model.Permission;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class Server {
    String status;
    List<Permission> permissionList;

public Server(){
    status="stop";
    permissionList = new ArrayList<>();
}
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPermissions(List<Permission> permissions, ModelMap map) {
    permissionList = permissions;

    for (Permission perm: permissionList) {
        map.addAttribute(perm.toString(), "true");
    }
    }

    public boolean permission(Permission permission) {
        for(Permission perm : permissionList) {
            if(perm == permission) {
                return true;
            }
        }
        return false;
    }
}
