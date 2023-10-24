package datasecurity.servicesImplementation;


import datasecurity.Persistence.DataBaseConnection;
import datasecurity.services.IAuthenticationService;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;

public class AuthenticationService extends UnicastRemoteObject implements IAuthenticationService, Serializable {
    int identifier;
    public AuthenticationService() throws RemoteException {
        super();
    }

    //TODO: implement password storage (System File, Public File, DBMS)
    //TODO: password verification DONE
    @Override
    public String authenticate(String username, String password) throws RemoteException, SQLException {

        //use secure KDF-based password hash - argon2id hybrid between 2d and ai
        //{salt+KDF(password,salt)} - Keep different random salt for each encrypted password+the key derived by KDF
        //Check:1.take the salt from the database
        //      2.derive a key from the password for checking
        //      3.compare the derived key with the key from the database.



        /*pasword here is BZHzPykd7gblOKC6INg8arz73VS7KbWDy+1Gem1iSKk=
        salt is MMq/BHKa5Lva/OwRduHi7g==
        String hashedPassword = "BZHzPykd7gblOKC6INg8arz73VS7KbWDy+1Gem1iSKk=";
        hashedPasswordFalse = "P+t76+uk9CfpI7o0iSZ/WA9hrHjKa6fJl3jZ1bFro6w=";
        String hashedPasswordSalt = "MMq/BHKa5Lva/OwRduHi7g==";
        String userPasswordTrue = "helloWorld";
        String userPasswordFalse = "bye";*/


        DataBaseConnection dataBaseConnection = new DataBaseConnection();

        String[] HashedPasswordSalt = dataBaseConnection.getHashedPasswordSalt(username);
        String hashedPassword = HashedPasswordSalt[1];
        String hashedPasswordSalt = HashedPasswordSalt[0];
        System.out.println("hashedPassword"+hashedPassword);
        System.out.println("hashedPasswordSalt"+hashedPasswordSalt);
        System.out.println("generatedHash"+generateHash(password, hashedPasswordSalt));

        if(generateHash(password, hashedPasswordSalt).matches(hashedPassword)){
            //user authenticated
            String cookie= String.valueOf(new Random().ints(12));
            Session session= new Session(username,cookie);
            RegistryBinder.bindPrintService(cookie);
            return cookie;
        }

        //user not authenticated
        return "false";
    }

    public static String generateHash(String password, String salt){

        int iterations = 50;
        int memLimit = 66536;
        int hashLength = 32;
        int parallelism = 1;

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iterations)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt.getBytes(StandardCharsets.UTF_8));


        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());
        byte[] result = new byte[hashLength];
        generate.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return Base64.getEncoder().encodeToString(result);
    }


}
