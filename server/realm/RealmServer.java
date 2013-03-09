package server.realm;

import java.nio.channels.SelectionKey;
import jelly.Jelly;
import server.InputThread;

public class RealmServer extends InputThread {
    private int count = 0;

    public RealmServer(){
        super(Jelly._config.REALM_PORT);
    }

    @Override
    protected void onCloseAction(SelectionKey key){

    }

    @Override
    protected void onConnectAction(SelectionKey key){
        key.attach(new core.Session(key));
    }

    @Override
    protected void onReadAction(SelectionKey key, String packet){
        count++;
    }
}
