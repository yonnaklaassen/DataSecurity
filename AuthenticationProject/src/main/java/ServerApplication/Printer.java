package ServerApplication;

import Services.PrintService;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Printer implements PrintService, Serializable {



    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void print(String filename, String printer) {
        System.out.println("printer"+printer+"starts printing file:"+filename);
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



