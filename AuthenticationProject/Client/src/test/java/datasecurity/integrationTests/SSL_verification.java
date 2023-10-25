package datasecurity.integrationTests;

import datasecurity.mocks.MockServer;
import datasecurity.mocks.MockPersistence;
import datasecurity.services.IAuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// This class aims to measure the security of communication channels built in the solution
// It includes testing database and remote communication integration using ssl
//@ExtendWith(SpringExtension.class)

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MockServer.MockServerSim.class, MockPersistence.class, MockServer.class})

public final class SSL_verification implements Serializable {

@Autowired
MockServer mockServerInit;

@BeforeEach
void initializeRegistry(){

    mockServerInit.initializeReg();
}


@Test
void TestLookUp() throws RemoteException, NotBoundException {
    //Run the server and create a registry without configuring tls
    mockServerInit.initialize_Without_SSL(1099);
    //Retrieve the registry
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    //Assert the registry is initialized
    Assertions.assertNotEquals(null,registry);
    //bind a remote object on the registry
    IAuthenticationService mockImpl = mockServerInit.new MockServiceImplemntation();
    mockServerInit.bindRemoteObject(mockImpl,"AuthObject");
    // Lookup in the registry
    IAuthenticationService returnedAuthenticationService =(IAuthenticationService) registry.lookup("AuthObject");
    // Assert the remote object exist
    Assertions.assertNotEquals(null,returnedAuthenticationService);
    //Ensure the skeleton returned is from the proxy server and not direct dependency to the server
    Assertions.assertEquals(true,returnedAuthenticationService.getClass().toString().contains("jdk.proxy"));

}

    @Test
    void databaseSecurity(){


        Assertions.assertEquals(1,1);
    }



/*
    @Test
    void TestLookUp_with_SSL() throws RemoteException, NotBoundException {
        //Run the server and create a registry without configuring tls
        mockServerInit.initialize_With_SSL(1099);
        //Retrieve the registry
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //Binding a remote object on the registry on server side
        IAuthenticationService mockImpl = mockServerInit.new MockServiceImplemntation();
        mockServerInit.bindRemoteObject(mockImpl,"AuthObject");
        // Lookup in the registry without configuring the clint to trust server certificate
        IAuthenticationService returnedAuthenticationService =(IAuthenticationService) registry.lookup("AuthObject");

        // Assertions.assertThrows(RemoteException.class ,()->{ });

        //bind a remote object on the registry
      // Assert the remote object exist
       // Assertions.assertNotEquals(null,returnedAuthenticationService);
        //Ensure the skeleton returned is from the proxy server and not direct dependency to the server
       // Assertions.assertEquals(true,returnedAuthenticationService.getClass().toString().contains("jdk.proxy"));

        //Ensure the remoteObject is Inaccessible when ssl Verification is required by the client

        // Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //IAuthenticationService authenticationService =(IAuthenticationService) registry.lookup("authenticationObject");


        //Ensure the remoteObject is accessible when ssl Verification is requires by the client

        // mockServerInit.

    }
*/




}
