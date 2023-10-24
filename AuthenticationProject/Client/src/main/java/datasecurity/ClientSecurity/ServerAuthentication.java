package datasecurity.ClientSecurity;

import datasecurity.communication.RemoteObjectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;


public class ServerAuthentication implements AuthenticationProvider {
    @Autowired
    RemoteObjectHandler remoteObjectHandler;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        boolean authStatus=false;
        try {
            if (isAuthenticationValid(username, password)) {

                authStatus=true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        if (authStatus) {

            UserDetails userDetails = new UsersConfig(username,"1");

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

    private boolean isAuthenticationValid(String username, String password) throws MalformedURLException, NotBoundException, RemoteException, SQLException {

        return remoteObjectHandler.getRemoteAuthenticationObject().authenticate(username,password);
    }


}
