package Services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintService extends Serializable,Remote {

    /**
     * Starts the print server
     */
    public void start();
    /**
     * Stops the print server
     */
    public void stop();
    /**
     * Stops the print server, clears the print queue and starts the print server again
     */
    public void restart();
    /**
     * Prints file 'filename' on the specified 'printer'
     */
    public void print(String filename, String printer);
    /**
     * Lists the print queue for a given printer on the user's display in lines of the form '< job number >< file name >'
     */
    public void queue(String printer);
    /**
     * Moves job to the top of the queue
     */
    public void topQueue(String printer, int job);
    /**
     * Prints status of printer on the user's display
     */
    public void status(String printer);
    /**
     * Prints the value of the parameter
     */
    public void readConfig(String parameter);
    /**
     * Sets the parameter on the print server to 'value'
     */
    public void setConfig(String parameter, String value);
}
