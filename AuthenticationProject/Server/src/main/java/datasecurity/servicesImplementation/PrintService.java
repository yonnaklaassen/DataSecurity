package datasecurity.servicesImplementation;




import datasecurity.Shared.ConsoleColors;
import model.Permission;
import datasecurity.model.PrintJob;
import datasecurity.model.Printer;
import datasecurity.services.IPrintService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintService extends UnicastRemoteObject  implements IPrintService {
    private List<Printer> printers;
    private Map<String, String> configurations;
    private String log;
    private int printJobCount;
    String remoteObjectLocalReference;

    public  PrintService(String refCookie) throws RemoteException {
        super();
        this.remoteObjectLocalReference=refCookie;
        log="";
        printers = new ArrayList<>();
        configurations = new HashMap<>();
        printJobCount = 0;
        //Add printers
        printers.add(new Printer("Printer1"));
        printers.add(new Printer("Printer2"));
        printers.add(new Printer("Printer3"));
    }

    @Override
    public void start() throws Exception{
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            log="Print server started\n";
            System.out.println(ConsoleColors.GREEN + "Print server started by user :" + username );
            setAllPrinterStatus(Printer.Status.ON);
    }

    @Override
    public void stop() throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            System.out.println(ConsoleColors.RED + "Print server stopped by user :" + username);
            setAllPrinterStatus(Printer.Status.OFF);
            log+="Print server stopped\n";
    }


    @Override
    public void restart() throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            log = "Print server restarted";
            System.out.println(ConsoleColors.ORANGE + "Print server is restarting  by user :" + Session.getSession(this.remoteObjectLocalReference).username);
            stop();
            setAllPrinterStatus(Printer.Status.RESTARTING);
            for (Printer printer : printers) {
                //Clear queue of all printers
                printer.clearQueue();
            }
            start();
    }

    @Override
    public void print(String filename, String printer) throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            printJobCount++;
            Printer p = findPrinter(printer);
            if(p != null)
                p.setStatus(Printer.Status.ON);
            p.addToQueue(new PrintJob(printJobCount, filename));

            log += printer+" starts printing file: "+filename+"\n";
            System.out.println(ConsoleColors.GREEN +printer+" starts printing file: "+filename +" by user :" + username);
    }


    @Override
    public void queue(String printer) throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            Printer p = findPrinter(printer);
            if(p != null){
                log += "Queue for "+printer+":\n"+ p.getQueue();
                // System.out.println(ConsoleColors.GREEN + p.getQueue());
            }
    }

    @Override
    public void topQueue(String printer, int job)throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            Printer p = findPrinter(printer);
            if(p != null){
                log += "Job: <"+ job + "> is on the top of the queue for printer :"+printer+"\n";
                p.addToTopOfQueue(job);
        }
    }

    @Override
    public void status(String printer) throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            Printer p = findPrinter(printer);
            if(p != null){
                log += "The status of " + printer + " is: " + p.getStatus()+"\n";
                System.out.println(ConsoleColors.GREEN + "The status of " + printer + " is: " + p.getStatus());}
    }

    @Override
    public void readConfig(String parameter) throws Exception{
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            log += "Configuration for Parameter: " +parameter + ", Value: " + configurations.get(parameter)+"\n";
            System.out.println(ConsoleColors.GREEN + "Parameter: " +parameter + ", Value: " + configurations.get(parameter));
    }

    @Override
    public void setConfig(String parameter, String value) throws Exception {
        validateSession();
        String username = Session.getSession(this.remoteObjectLocalReference).username;
            log += "Parameter: "+parameter+", Value: "+value+"\n";
            System.out.println("configurations for parameter"+parameter+". by user :" + username);
            configurations.put(parameter, value);
    }

    @Override
    public String getPrintLog(){
        return this.log;
    }


    public Printer findPrinter(String printerName) {
        for (Printer p : printers) {
            if(p.getName().equals(printerName)) {
                return p;
            }
        }
        return null;
    }

    public void setAllPrinterStatus(Printer.Status status) {
        for (Printer printer : printers) {
            printer.setStatus(status);
        }
    }

    private void validateSession() throws Exception {
        Session session=Session.getSession(this.remoteObjectLocalReference);

        if (session.isTimedOut()){
           RegistryBinder.unBindPrintService(this.remoteObjectLocalReference);
            System.out.println("Session is timed out\n"+"Print service is down");
           throw new Exception("The session is timed out, please log-in again");
        }else {
            session.prolongSession();
        }
    }
    public Map<String, String> getConfigurations() {
        return configurations;
    }
}



