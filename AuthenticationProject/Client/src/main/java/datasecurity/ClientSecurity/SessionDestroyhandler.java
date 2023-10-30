package datasecurity.ClientSecurity;

import datasecurity.models.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class SessionDestroyhandler implements HttpSessionListener {
Server server;
UsersConfig usersConfig;

    @Autowired
    public SessionDestroyhandler(UsersConfig usersConfig,Server _server) {

        this.usersConfig = usersConfig;
        this.server=_server;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        usersConfig.seUsername("");
        usersConfig.set_sessionAuthCookie("");
        usersConfig._isActiveSession=false;
        server.setStatus("stop");
        System.out.println("\u001B[31m"+"Session is destroyed"+"\u001B[37m");
        SecurityContextHolder.clearContext();

    }



}
