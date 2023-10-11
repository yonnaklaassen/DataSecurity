package Services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintService extends Serializable,Remote {

    public void start();
    public void stop();
    public void restart();
    public void print(String filename, String printer);
    public void queue(String printer);
    public void topQueue(String printer, int job);
    public void status(String printer);
    public void readConfig(String parameter);
    public void setConfig(String parameter, String value);
}
