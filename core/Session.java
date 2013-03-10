package core;

import java.nio.channels.SelectionKey;
import server.InputThread;

public class Session extends server.Client {
    private boolean login = false;

    public Session(SelectionKey key, InputThread server){
        super(key, server);
    }

    public void logout(){
        login = false;
    }

    public boolean isLogin(){
        return login;
    }
}
