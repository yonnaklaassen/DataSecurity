package datasecurity.Controller;


import datasecurity.communication.RemoteObjectHandler;

import datasecurity.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Controller
public class HomeController {

private Server server;
private RemoteObjectHandler rmh;
private String authURl;
private  String printServiceUrl;

@Autowired
public HomeController(Server _server, RemoteObjectHandler _rmh){
    server=_server;
    rmh=_rmh;


}

    @RequestMapping("/login")
    public  String login(){
    return "login";
}


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public  String home(ModelMap map)
    {
       // IPrintService printService = Naming.lookup("rmi://localhost:8080/printerObject");

        map.addAttribute("status",server.getStatus());
        return "home";
    }



    @RequestMapping("/startServer")
        public ResponseEntity<String> startServer(ModelMap map) throws MalformedURLException, NotBoundException, RemoteException {
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
    public ResponseEntity<String> restartServer(){
        try {
            rmh.getRemotePrintServiceObject().restart();
        }catch (Exception e){
            return ResponseEntity.ok("{\"data\":\""+ e.getMessage()+"\"}");
        }
        server.setStatus("start");
        return ResponseEntity.ok("{\"data\":\"server restarted\"}");

    }


    @RequestMapping("/print")

    public ResponseEntity<String> print( @RequestParam("printer") String printer,
                                         @RequestParam("file") String file) throws MalformedURLException, NotBoundException, RemoteException {

        System.out.println(printer+"   "+file);
    try {
            rmh.getRemotePrintServiceObject().print(file,printer);
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
        System.out.println(rmh.getRemotePrintServiceObject().getPrintLog());
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/queue")
    public ResponseEntity<String> queue( @RequestParam("printer") String printer) throws MalformedURLException, NotBoundException, RemoteException {
        rmh.getRemotePrintServiceObject().queue(printer);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }
    @RequestMapping("/topQueue")
    public ResponseEntity<String> topQueue( @RequestParam("printer") String printer, @RequestParam("job") int job) throws MalformedURLException, NotBoundException, RemoteException {
        rmh.getRemotePrintServiceObject().topQueue(printer,job);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/status")
    public ResponseEntity<String> status( @RequestParam("printer") String printer) throws MalformedURLException, NotBoundException, RemoteException {
        rmh.getRemotePrintServiceObject().status(printer);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/setConf")
    public ResponseEntity<String> setConf( @RequestParam("parameter") String parameter, @RequestParam("value") String value) throws MalformedURLException, NotBoundException, RemoteException {
        rmh.getRemotePrintServiceObject().setConfig(parameter,value);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    @RequestMapping("/getConf")
    public ResponseEntity<String> getConf( @RequestParam("parameter") String parameter) throws MalformedURLException, NotBoundException, RemoteException {
        rmh.getRemotePrintServiceObject().readConfig(parameter);
        return ResponseEntity.ok(rmh.getRemotePrintServiceObject().getPrintLog());

    }

    {

}



}
