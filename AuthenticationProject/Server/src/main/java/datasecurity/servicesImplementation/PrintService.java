package datasecurity.servicesImplementation;




import datasecurity.Shared.ConsoleColors;
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
    //TODO: implement print out the jobs from the printer queues.
    //TODO: consider logger or simple console print outs for each action
    private List<Printer> printers;
    private Map<String, String> configurations;
    private String log;
    private int printJobCount;

    public  PrintService() throws RemoteException {
        super();
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
    public void start() {
        log="Print server started\n";
        System.out.println(ConsoleColors.GREEN + "Print server started");
        setAllPrinterStatus(Printer.Status.ON);
    }

    @Override
    public void stop() {
        System.out.println(ConsoleColors.RED + "Print server stopped");
        setAllPrinterStatus(Printer.Status.OFF);
        log+="Print server stopped\n";
    }


    @Override
    public void restart() {
        log = "Print server restarted";
        System.out.println(ConsoleColors.ORANGE + "Print server is restarting");
        stop();
        setAllPrinterStatus(Printer.Status.RESTARTING);
        for (Printer printer : printers) {
            //Clear queue of all printers
            printer.clearQueue();
        }
        start();
    }

    @Override
    public void print(String filename, String printer) {
        printJobCount++;
        Printer p = findPrinter(printer);
        if(p != null)
            p.setStatus(Printer.Status.ON);
            p.addToQueue(new PrintJob(printJobCount, filename));

        log += printer+" starts printing file: "+filename+"\n";
        System.out.println(ConsoleColors.GREEN +printer+" starts printing file: "+filename);
    }

    public static void main(String[] args) throws RemoteException {

        PrintService printService = new PrintService();
       // printService.status("printer #1");
       // printService.print("test","printer #1");
       // printService.status("printer #1");
       // printService.queue("printer #1");

        System.out.println(printService.log);
    }
    @Override
    public void queue(String printer) {
        Printer p = findPrinter(printer);
        if(p != null){
            log += "Queue for "+printer+":\n"+ p.getQueue();
            System.out.println(ConsoleColors.GREEN + p.getQueue());
        }

    }

    @Override
    public void topQueue(String printer, int job) {
        Printer p = findPrinter(printer);
        if(p != null)
            log += "Job: <"+ job + "> is on the top of the queue for printer :"+printer+"\n";
            p.addToTopOfQueue(job);
    }

    @Override
    public void status(String printer) {
        Printer p = findPrinter(printer);
        if(p != null){
            log += "The status of " + printer + " is: " + p.getStatus()+"\n";
            System.out.println(ConsoleColors.GREEN + "The status of " + printer + " is: " + p.getStatus());}
    }

    @Override
    public void readConfig(String parameter) {
        log += "Configuration for Parameter: " +parameter + ", Value: " + configurations.get(parameter)+"\n";
        System.out.println(ConsoleColors.GREEN + "Parameter: " +parameter + ", Value: " + configurations.get(parameter));
    }

    @Override
    public void setConfig(String parameter, String value) {
        log += "Parameter: "+parameter+", Value: "+value+"\n";

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

    public Map<String, String> getConfigurations() {
        return configurations;
    }
}



