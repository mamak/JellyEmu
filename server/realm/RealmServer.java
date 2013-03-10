package server.realm;

import core.Config;
import java.nio.channels.SelectionKey;
import server.InputThread;

public class RealmServer extends InputThread {
    private int count = 0;

    public RealmServer(){
        super(Config.getInt("realm_port", 0, 65536));
    }

    @Override
    protected void onCloseAction(SelectionKey key){
        ((core.Session)key.attachment()).logout();
    }

    @Override
    protected void onConnectAction(SelectionKey key){
        key.attach(new core.Session(key, this));
    }

    @Override
    protected void onReadAction(SelectionKey key, String packet){
        count++;
    }
}
