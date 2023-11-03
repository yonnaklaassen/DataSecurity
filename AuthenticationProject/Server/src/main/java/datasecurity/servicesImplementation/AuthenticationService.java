package datasecurity.servicesImplementation;


import datasecurity.Persistence.DataBaseConnection;
import datasecurity.services.IAuthenticationService;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    public String authenticate(String username, String password) throws RemoteException, SQLException, NoSuchAlgorithmException {

        DataBaseConnection dataBaseConnection = new DataBaseConnection();

        String[] HashedPasswordAndSalt = dataBaseConnection.getHashedPasswordSalt(username);
        String hashedPassword = HashedPasswordAndSalt[1];
        String salt = HashedPasswordAndSalt[0];
        System.out.println("Start authenticating User:"+" "+username);

        if(generateHash(password, salt).matches(hashedPassword)){
            //user authenticated
            SecureRandom secureRandom =SecureRandom.getInstance("SHA1PRNG");
            String cookie= String.valueOf(secureRandom.nextInt());
            String encryptedCookie= generateHash(cookie,salt);
            Session session= new Session(username,encryptedCookie);
            RegistryBinder.bindPrintService(encryptedCookie);
            return encryptedCookie;
        }

        //user not authenticated
        System.out.println("User :"+username+" is not authenticated");
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
