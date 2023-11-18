package datasecurity.Controller;


import datasecurity.ClientSecurity.UsersConfig;
import datasecurity.communication.RemoteObjectHandler;

import model.Permission;
import datasecurity.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    public  String login() throws MalformedURLException, NotBoundException, RemoteException {
        System.out.println(usersConfig.activeSession()+"--------"+usersConfig.getUsername());
        if (usersConfig.sessionStutus()){
            List<Permission> permissions = rmh.getAccessControlServiceObject().getPermissionsByUser(usersConfig.getUsername());
            server.setPermissions(permissions);
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
    public  String home(ModelMap map)
    {
        if (usersConfig.getUsername()!=null){
            map.addAttribute("username",usersConfig.getUsername());
        }
        map.addAttribute("status",server.getStatus());
        return "home";
    }



    @RequestMapping("/startServer")
        public ResponseEntity<String> startServer(ModelMap map)  {

        try {
            rmh.getRemotePrintServiceObject().start();
        }catch (Exception e){
            return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
        }
        server.setStatus("start");
        map.addAttribute("status","start");

        return ResponseEntity.ok("{\"data\":\" Server started \"}");

        }

    @RequestMapping("/stopServer")
    public ResponseEntity<String> stopServer(){

        try {
            rmh.getRemotePrintServiceObject().stop();
        }catch (Exception e){
            return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
        }
        server.setStatus("stop");

        return ResponseEntity.ok("{\"data\":\"server stopped\"}");

    }
    @RequestMapping("/restartServer")
    public ResponseEntity<String> restartServer() throws Exception {

            try {
                rmh.getRemotePrintServiceObject().restart();
            } catch (Exception e) {
                return ResponseEntity.ok("{\"data\":\"" + e.getMessage() + "\"}");
            }

        server.setStatus("start");
        rmh.getRemotePrintServiceObject().restart();

        return ResponseEntity.ok("{\"data\":\"server restarted\"}");

    }


    @RequestMapping("/print")

    public ResponseEntity<String> print(@RequestParam("printer") String printer,
                                        @RequestParam("file") String file) throws Exception {

        try{
               rmh.getRemotePrintServiceObject().print(file,printer);

        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/queue")
    public ResponseEntity<String> queue( @RequestParam("printer") String printer) throws Exception {
        rmh.getRemotePrintServiceObject().queue(printer);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @RequestMapping("/topQueue")
    public ResponseEntity<String> topQueue( @RequestParam("printer") String printer, @RequestParam("job") int job) throws Exception {
        rmh.getRemotePrintServiceObject().topQueue(printer,job);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/status")
    public ResponseEntity<String> status( @RequestParam("printer") String printer) throws Exception {
        rmh.getRemotePrintServiceObject().status(printer);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/setConf")
    public ResponseEntity<String> setConf( @RequestParam("parameter") String parameter, @RequestParam("value") String value) throws Exception {
        rmh.getRemotePrintServiceObject().setConfig(parameter,value);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/getConf")
    public ResponseEntity<String> getConf( @RequestParam("parameter") String parameter) throws Exception {
        rmh.getRemotePrintServiceObject().readConfig(parameter);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    {

}



}
