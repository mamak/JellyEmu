package server.realm;

import core.Config;
import java.nio.channels.SelectionKey;
import jelly.Constants;
import server.Client;
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

        switch(count){
            //version dofus
            case 1:
                if(!packet.trim().equals(Constants.VERSION)){
                    RealmPacketEnum.CLIENT_VERSION_ERROR.send((Client)key.attachment(), Constants.VERSION);
                }
                break;
            //Nom de compte
            case 2:
                break;
            //mot de passe
            case 3:
                break;
        }
    }
}
