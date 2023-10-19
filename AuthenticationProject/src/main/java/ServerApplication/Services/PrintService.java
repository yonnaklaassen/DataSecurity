package ServerApplication.Services;

import ServerApplication.Model.PrintJob;
import ServerApplication.Model.Printer;
import Shared.ConsoleColors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintService implements IPrintService, Serializable {
    //TODO: implement print out the jobs from the printer queues.
    //TODO: consider logger or simple console print outs for each action
    private List<Printer> printers;
    private Map<String, String> configurations;

    private int printJobCount;

    public  PrintService() {
        printers = new ArrayList<>();
        configurations = new HashMap<>();
        printJobCount = 0;
        //Add printers
        printers.add(new Printer("Printer #1"));
        printers.add(new Printer("Printer #2"));
        printers.add(new Printer("Printer #3"));
    }

    @Override
    public void start() {
        System.out.println(ConsoleColors.GREEN + "Print server started");
        setAllPrinterStatus(Printer.Status.ON);
    }

    @Override
    public void stop() {
        System.out.println(ConsoleColors.RED + "Print server stopped");
        setAllPrinterStatus(Printer.Status.OFF);
    }

    @Override
    public void restart() {
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
        Printer p = findPrinter(printer);
        if(p != null) p.addToQueue(new PrintJob(printJobCount, filename));
        printJobCount++;

        System.out.println(ConsoleColors.GREEN + "Printer "+printer+" starts printing file: "+filename);
    }

    @Override
    public void queue(String printer) {
        Printer p = findPrinter(printer);
        if(p != null) System.out.println(ConsoleColors.GREEN + p.getQueue());
    }

    @Override
    public void topQueue(String printer, int job) {
        Printer p = findPrinter(printer);
        if(p != null) p.addToTopOfQueue(job);
    }

    @Override
    public void status(String printer) {
        Printer p = findPrinter(printer);
        if(p != null) System.out.println(ConsoleColors.GREEN + "The status of " + printer + " is: " + p.getStatus());
    }

    @Override
    public void readConfig(String parameter) {
        System.out.println(ConsoleColors.GREEN + "Parameter: " +parameter + ", Value: " + configurations.get(parameter));
    }

    @Override
    public void setConfig(String parameter, String value) {
        configurations.put(parameter, value);
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



