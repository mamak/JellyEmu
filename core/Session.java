package core;

import java.nio.channels.SelectionKey;

public class Session {
    private SelectionKey key;
    private boolean login = false;

    public Session(SelectionKey key){
        this.key = key;
    }

    public SelectionKey getKey(){
        return key;
    }

    public boolean isLogin(){
        return login;
    }
}
