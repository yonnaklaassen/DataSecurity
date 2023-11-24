package datasecurity.ClientSecurity;

import datasecurity.communication.RemoteObjectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;


public class ServerAuthentication implements AuthenticationProvider {
    @Autowired
    RemoteObjectHandler remoteObjectHandler;
    @Autowired
    UsersConfig userDetails;


    //@lombok.SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        boolean authStatus=false;
        try {
            authStatus=isAuthenticationValid(username,password);
        }catch (Exception e){
            e.printStackTrace();
        }


        if (authStatus) {

            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } else {
            // Authentication failed
            throw new UsernameNotFoundException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isAuthenticationValid(String username, String password) throws Exception {
        String authStatus= remoteObjectHandler.getRemoteAuthenticationObject().authenticate(username,password);
        if(!authStatus.equals("false")){
            userDetails.set_sessionAuthCookie(decrypt(authStatus,"MySecretKey123456789012345678901"));
            userDetails.seUsername(username);
            userDetails._isActiveSession=true;
            return true;
        }
        return false;

    }
    public static String decrypt(String encryptedValue, String secretKey) throws Exception {
        byte[] decryptedBytes;

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedValue);
        decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }


}
