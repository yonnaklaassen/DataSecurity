package datasecurity.Controller;


import datasecurity.ClientSecurity.UsersConfig;
import datasecurity.communication.RemoteObjectHandler;

import model.Permission;
import datasecurity.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

@Controller
public class HomeController {
private UsersConfig usersConfig;
private Server server;
private RemoteObjectHandler rmh;


@Autowired
public HomeController(Server _server, RemoteObjectHandler _rmh, UsersConfig _userConfig){
    server=_server;
    rmh=_rmh;
    usersConfig=_userConfig;

}
    @RequestMapping("/")
    public  String root()
    {
        return "redirect:/home";
    }

    @RequestMapping("/login")
    public  String login() {
        if (usersConfig.sessionStutus()){
        //    System.out.println(usersConfig.activeSession()+"home--------"+usersConfig.getUsername());
            return "home";
        }else {
            return "login";
        }
}
    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public  String home(ModelMap map) throws NotBoundException, RemoteException {
        if (usersConfig.getUsername()!=null){
            map.addAttribute("username",usersConfig.getUsername());
            List<Permission> permissions = rmh.getAccessControlServiceObject().getPermissionsByUser(usersConfig.getUsername());
            server.setPermissions(permissions, map);
        }
        map.addAttribute("status",server.getStatus());
        return "home";
    }

    @RequestMapping("/startServer")
        public ResponseEntity<String> startServer(ModelMap map)  {
        if(server.permission(Permission.START)) {
            try {
                rmh.getRemotePrintServiceObject().start();
            }catch (Exception e){
                return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
            }
            server.setStatus("start");
            map.addAttribute("status","start");

            return ResponseEntity.ok("{\"data\":\" Server started \"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
        }

    @RequestMapping("/stopServer")
    public ResponseEntity<String> stopServer(){
        if(server.permission(Permission.STOP)) {

            try {
                rmh.getRemotePrintServiceObject().stop();
            }catch (Exception e){
                return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
            }
            server.setStatus("stop");

            return ResponseEntity.ok("{\"data\":\"server stopped\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }
    @RequestMapping("/restartServer")
    public ResponseEntity<String> restartServer(ModelMap map) throws Exception {
        if(server.permission(Permission.RESTART)) {
            try {
                rmh.getRemotePrintServiceObject().restart();
            } catch (Exception e) {
                return ResponseEntity.ok("{\"data\":\"" + e.getMessage() + "\"}");
            }

            server.setStatus("start");
            rmh.getRemotePrintServiceObject().restart();

            return ResponseEntity.ok("{\"data\":\"server restarted\"}");
        }

        map.addAttribute("restart", "false");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }


    @RequestMapping("/print")

    public ResponseEntity<String> print(@RequestParam("printer") String printer,
                                        @RequestParam("file") String file) throws Exception {

        if(server.permission(Permission.PRINT)) {
            try{
                rmh.getRemotePrintServiceObject().print(file,printer);
            }catch (Exception e){
                return ResponseEntity.ok(e.getMessage());
            }
            return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @RequestMapping("/queue")
    public ResponseEntity<String> queue( @RequestParam("printer") String printer) throws Exception {
        if(server.permission(Permission.QUEUE)) {
            rmh.getRemotePrintServiceObject().queue(printer);
            return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }
    @RequestMapping("/topQueue")
    public ResponseEntity<String> topQueue( @RequestParam("printer") String printer, @RequestParam("job") int job) throws Exception {
        if(server.permission(Permission.TOP_QUEUE)) {
            rmh.getRemotePrintServiceObject().topQueue(printer,job);
            return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @RequestMapping("/status")
    public ResponseEntity<String> status( @RequestParam("printer") String printer) throws Exception {
        rmh.getRemotePrintServiceObject().status(printer);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
    }

    @RequestMapping("/setConf")
    public ResponseEntity<String> setConf( @RequestParam("parameter") String parameter, @RequestParam("value") String value) throws Exception {
        if(server.permission(Permission.SET_CONFIG)) {
            rmh.getRemotePrintServiceObject().setConfig(parameter,value);
            return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }

    @RequestMapping("/getConf")
    public ResponseEntity<String> getConf( @RequestParam("parameter") String parameter) throws Exception {
        if(server.permission(Permission.READ_CONFIG)) {
            rmh.getRemotePrintServiceObject().readConfig(parameter);
            return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
    }
}
