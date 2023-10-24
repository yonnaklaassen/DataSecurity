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
    @Autowired
    UsersConfig userDetails;


    @lombok.SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        boolean authStatus=isAuthenticationValid(username,password);

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

    private boolean isAuthenticationValid(String username, String password) throws MalformedURLException, NotBoundException, RemoteException, SQLException {
        String authStatus= remoteObjectHandler.getRemoteAuthenticationObject().authenticate(username,password);
        if(authStatus!="false"){

            userDetails.set_sessionAuthCookie(authStatus);
            return true;
        }
        return false;

    }


}
