package ServerApplication;

import Services.PrintService;
import Shared.ConsoleColors;

import java.io.Serializable;

public class Printer implements PrintService, Serializable {

    @Override
    public void start() {
        System.out.println(ConsoleColors.GREEN + "Print server started");
    }

    @Override
    public void stop() {
        System.out.println(ConsoleColors.RED + "Print server stopped");
    }

    @Override
    public void restart() {
        System.out.println(ConsoleColors.ORANGE + "Print server is restarting");
    }

    @Override
    public void print(String filename, String printer) {
        System.out.println(ConsoleColors.GREEN + "Printer "+printer+" starts printing file: "+filename);
    }

    @Override
    public void queue(String printer) {

    }

    @Override
    public void topQueue(String printer, int job) {

    }

    @Override
    public void status(String printer) {

    }

    @Override
    public void readConfig(String parameter) {

    }

    @Override
    public void setConfig(String parameter, String value) {

    }
}



