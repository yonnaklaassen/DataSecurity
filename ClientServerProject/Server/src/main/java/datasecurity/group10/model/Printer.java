package datasecurity.group10.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;

public class Printer implements Serializable {
    private final String name;
    private LinkedList<PrintJob> printQueue;
    private Status status;

    public Printer(String name) {
        this.name = name;
        printQueue = new LinkedList<>();
        status = Status.OFF;
    }

    public String getQueue() {
        StringBuilder print = new StringBuilder();
    for(PrintJob job : printQueue) {
        print.append("<").append(job.getJob()).append("> <").append(job.getFileName()).append(">").append("\n");
    }
    return print.toString();
    }

    public void addToQueue(PrintJob job) {
        printQueue.add(job);
    }

    public void addToTopOfQueue(int jobNumber) {
        for(PrintJob job : printQueue) {
            if(job.getJob() == jobNumber) {
                printQueue.remove(job);
                printQueue.addFirst(job);
            }
        }
    }

    public String getName() {
        return  name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public void clearQueue() {
        printQueue = new LinkedList<>();
    }

    public enum Status {
        ON, OFF, RESTARTING
    }
}

