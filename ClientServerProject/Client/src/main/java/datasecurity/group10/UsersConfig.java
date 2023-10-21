package datasecurity.group10;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsersConfig implements UserDetails {

    String _username;
    String _sessionID;

    public UsersConfig(String username,String sessionID){
        _username=username;
        _sessionID=sessionID;
    }

    public String get_sessionID(){
        return _sessionID;
    }

    public void set_sessionID(String sessionID){
        _sessionID=sessionID;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return _username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
