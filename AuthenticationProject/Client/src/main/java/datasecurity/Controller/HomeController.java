package datasecurity.Controller;


import datasecurity.ClientSecurity.UsersConfig;
import datasecurity.communication.RemoteObjectHandler;

import datasecurity.models.Server;
import datasecurity.session.SessionDestroyhandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (usersConfig.getUsername()!=null){
            map.addAttribute("username",usersConfig.getUsername());
        }
        map.addAttribute("status",server.getStatus());
        return "home";
    }



    @RequestMapping("/startServer")
        public ResponseEntity<String> startServer(ModelMap map) throws MalformedURLException, NotBoundException, RemoteException {

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

    @RequestMapping("/stopServer")
    public ResponseEntity<String> stopServer(){

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
    @RequestMapping("/restartServer")
    public ResponseEntity<String> restartServer() throws Exception {

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


    @RequestMapping("/print")

    public ResponseEntity<String> print(@RequestParam("printer") String printer,
                                        @RequestParam("file") String file) throws Exception {

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

    @RequestMapping("/queue")
    public ResponseEntity<String> queue( @RequestParam("printer") String printer) throws Exception {
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
    @RequestMapping("/topQueue")
    public ResponseEntity<String> topQueue( @RequestParam("printer") String printer, @RequestParam("job") int job) throws Exception {
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

    @RequestMapping("/status")
    public ResponseEntity<String> status( @RequestParam("printer") String printer) throws Exception {
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

    @RequestMapping("/setConf")
    public ResponseEntity<String> setConf( @RequestParam("parameter") String parameter, @RequestParam("value") String value) throws Exception {

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

    @RequestMapping("/getConf")
    public ResponseEntity<String> getConf( @RequestParam("parameter") String parameter) throws Exception {
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

    {

}



}
