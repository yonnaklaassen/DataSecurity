package datasecurity.group10;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ResourceServerAuthentication implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("Authhhhh");
        // Perform your external validation here (e.g., send a request to another server)
        if (isAuthenticationValid(username, password)) {
            // Authentication successful

            //UserDetails userDetails = loadUserByUsername(username);
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

    private boolean isAuthenticationValid(String username, String password)  {


        if (username.equals("abc")&&password.equals("123")){
            return true;
        }
        // Implement your logic to validate the username and password with the external server
        // Return true if authentication is valid, false otherwise
        // You can use HTTP client libraries like RestTemplate or WebClient to make the external request
        // Example: Make an HTTP request to another server to validate the credentials
        // Replace this with your actual validation logic
        // if (externalServer.validate(username, password)) {
        //     return true;
        // }
        // return false;
        return false; // Replace with your authentication logic
    }


}
