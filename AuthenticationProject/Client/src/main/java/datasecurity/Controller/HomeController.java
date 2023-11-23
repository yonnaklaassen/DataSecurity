package datasecurity.Controller;


import datasecurity.session.SessionDestroyhandler;
import datasecurity.ClientSecurity.UsersConfig;
import datasecurity.communication.RemoteObjectHandler;

import datasecurity.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;

@Controller
public class HomeController {
    private UsersConfig usersConfig;
    private Server server;
    private RemoteObjectHandler rmh;
    private SessionDestroyhandler sessionDestroyhandler;


    @Autowired
    public HomeController(Server _server, RemoteObjectHandler _rmh, UsersConfig _userConfig,SessionDestroyhandler _sessionDestroyhandler){
        server=_server;
        rmh=_rmh;
        usersConfig=_userConfig;
        sessionDestroyhandler=_sessionDestroyhandler;
    }
    @RequestMapping("/")
    public  String root()
    {

        return "redirect:/home";
    }
    @RequestMapping("/login")
    public  String login()
    {
        //System.out.println(usersConfig.activeSession()+"--------"+usersConfig.getUsername());
        if (usersConfig.sessionStutus()){
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
        map.addAttribute("userRole",usersConfig.userRole.toString());
        map.addAttribute("username",usersConfig.getUsername());
        map.addAttribute("status",server.getStatus());

        return "home";
    }



    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician')")
    @RequestMapping("/startServer")
    public ResponseEntity<String> startServer(ModelMap map) throws MalformedURLException, NotBoundException, RemoteException {
        map.addAttribute("role",usersConfig.userRole);


        try {
            rmh.getRemotePrintServiceObject().start();
        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
        }
        server.setStatus("start");
        map.addAttribute("status","start");

        return ResponseEntity.ok("{\"data\":\" Server started \"}");

    }
    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician')")
    @RequestMapping("/stopServer")
    public ResponseEntity<String> stopServer(ModelMap map){
        map.addAttribute("role",usersConfig.userRole);

        try {
            rmh.getRemotePrintServiceObject().stop();
        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
        }
        server.setStatus("stop");

        return ResponseEntity.ok("{\"data\":\"server stopped\"}");

    }
    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician') and hasRole('powerUser')")
    @RequestMapping("/restartServer")
    public ResponseEntity<String> restartServer(ModelMap map) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try {
            rmh.getRemotePrintServiceObject().restart();
        } catch (Exception e) {
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok("{\"data\":\"" + e.getMessage() + "\"}");
        }

        server.setStatus("start");
        rmh.getRemotePrintServiceObject().restart();
        return ResponseEntity.ok("{\"data\":\"server restarted\"}");

    }

    @PreAuthorize("hasRole('admin') and hasRole('powerUser') and hasRole('user')")
    @RequestMapping("/print")

    public ResponseEntity<String> print(ModelMap map,@RequestParam("printer") String printer,
                                        @RequestParam("file") String file) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().print(file,printer);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);


            }
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @PreAuthorize("hasRole('admin') and hasRole('powerUser') and hasRole('user')")
    @RequestMapping("/queue")
    public ResponseEntity<String> queue(ModelMap map, @RequestParam("printer") String printer) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().queue(printer);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);
            }

            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @PreAuthorize("hasRole('admin') and hasRole('powerUser')")
    @RequestMapping("/topQueue")
    public ResponseEntity<String> topQueue(ModelMap map, @RequestParam("printer") String printer, @RequestParam("job") int job) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().topQueue(printer,job);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok(e.getMessage());
        }


        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician')")
    @RequestMapping("/status")
    public ResponseEntity<String> status(ModelMap map, @RequestParam("printer") String printer) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().status(printer);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician')")
    @RequestMapping("/setConf")
    public ResponseEntity<String> setConf(ModelMap map, @RequestParam("parameter") String parameter, @RequestParam("value") String value) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().setConfig(parameter,value);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @PreAuthorize("hasRole('admin') and hasRole('serviceTechnician')")
    @RequestMapping("/getConf")
    public ResponseEntity<String> getConf(ModelMap map, @RequestParam("parameter") String parameter) throws Exception {
        map.addAttribute("role",usersConfig.userRole);

        try{
            rmh.getRemotePrintServiceObject().readConfig(parameter);

        }catch (Exception e){
            if (e.getMessage().contains("The session is timed out, please log-in again")){
                SecurityContextHolder.clearContext();
                return new ResponseEntity<>( HttpStatus.FOUND);

            }
            return ResponseEntity.ok(e.getMessage());
        }


        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }



}
