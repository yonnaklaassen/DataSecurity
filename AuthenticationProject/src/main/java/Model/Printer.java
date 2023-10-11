package Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Printer implements Serializable {
    private String name;
    private LinkedList<String> queue;
    private Map<String, String> configurations;
    private String status;

    public Printer(String name) {
        this.name = name;
        queue = new LinkedList<>();
        configurations = new HashMap<>();
        status = "Turned off";
    }
}
