package Services;

import java.io.Serializable;
import java.rmi.Remote;

public interface IPrintService extends Serializable,Remote {

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
     * @param filename the file name
     * @param printer the printer name
     */
    public void print(String filename, String printer);
    /**
     * Lists the print queue for a given printer on the user's display in lines of the form '< job number >< file name >'
     * @param printer the printer name
     */
    public void queue(String printer);
    /**
     * Moves job to the top of the queue
     * @param printer the printer name
     * @param job the job number
     */
    public void topQueue(String printer, int job);
    /**
     * Prints status of printer on the user's display
     * @param printer the printer name
     */
    public void status(String printer);
    /**
     * Prints the value of the parameter
     * @param parameter value to be printed
     */
    public void readConfig(String parameter);
    /**
     * Sets the parameter on the print server to 'value'
     * @param parameter the parameter to be set
     * @param value the value to be set to the parameter
     */
    public void setConfig(String parameter, String value);
}

