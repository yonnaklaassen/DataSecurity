package datasecurity.group10;


import datasecurity.group10.Models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {


@Autowired
Server server;

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


    @PostMapping("/authenticateFromResourceServer")
            public String auth(String username, String password){

        System.out.println(username+password);
        return "redirect:home";
    }

    @RequestMapping("/startServer")
        public ResponseEntity<String> startServer(ModelMap map){
        server.setStatus("start");
        map.addAttribute("status","start");

        return ResponseEntity.ok("{\"data\":\"server is ready\"}");

        }

    @RequestMapping("/stopServer")
    public ResponseEntity<String> stopServer(){
    server.setStatus("stop");
        return ResponseEntity.ok("{\"data\":\"server is stopped\"}");

    }



    {

}



}
