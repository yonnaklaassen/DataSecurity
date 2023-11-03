package datasecurity.servicesImplementation;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Session {

    static HashMap<String,Session> sessionMapper= new HashMap<>();
    String remoteMappingCookie;
    boolean isActive = false;

    //how long is the session
    int duration = 30*1000;
    long activationTime;

    String username;

    public Session( String username,String refCookie) {
        sessionMapper.put(refCookie,this);
        this.username = username;
        this.activationTime=System.currentTimeMillis();
    }


    public static Session getSession(String cookie){
        for (Map.Entry<String,Session> e:sessionMapper.entrySet()) {
            if (e.getKey().equals(cookie)){
                return e.getValue();
            }
        }
        return null;
    }

    public boolean isTimedOut(){
        //needs session validation here and one step before - rehashing session from client
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime-this.activationTime;
        if(timeElapsed < this.duration){
            this.isActive = true;
            return false;
        }

        return true;
    }

    public void prolongSession(){
        this.activationTime=System.currentTimeMillis();
    }


}
