package datasecurity.mocks;

import datasecurity.services.IAuthenticationService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

@Component
public class MockServer implements Serializable {
    Registry registry;

    public void bindRemoteObject(Remote remoteInterFace, String objectName) throws RemoteException {

        registry.rebind(objectName,remoteInterFace);
    }


    public void initialize_With_SSL(int port)  {
        MockServerSim m = new MockServerSim();
        Thread t = new Thread(m);
       try {


           System.setProperty("javax.net.ssl.keyStorePassword", "group10");

           System.setProperty("javax.net.ssl.keyStore", "keyStoreForTest.pfx");

           registry = LocateRegistry.createRegistry(1099, null, new SslRMIServerSocketFactory());
           t.start();
       }catch (Exception e){
           System.out.println("Exception fired");
           e.printStackTrace();
           t.interrupt();

       }
    }


    public void initialize_Without_SSL(int port) throws RemoteException {
         registry = LocateRegistry.createRegistry(port);


        MockServerSim m = new MockServerSim();
        Thread t = new Thread(m);
        t.start();
    }

    public void initializeReg() {

        registry=null;
    }

    public class MockServerSim implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            boolean active = true;

            while (active) {
                System.out.println("Server is started");

                if (Thread.interrupted()) {
                    active = false;
                    System.out.println("Server is stopped");
                }
                Thread.sleep(1000);

            }
        }

    }


    public class MockServiceImplemntation extends UnicastRemoteObject implements IAuthenticationService {


        public MockServiceImplemntation() throws RemoteException {
            super();
        }

        @Override
        public String authenticate(String username, String password) throws RemoteException, SQLException {

            return null;
        }
    }
}
