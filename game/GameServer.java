package game;

import java.net.ServerSocket;
import java.util.ArrayList;
import jelly.Jelly;
import system.Debug;

public class GameServer extends Thread {
    protected ArrayList<GameThread> _clients = new ArrayList<>();
    protected ServerSocket _server;

    public GameServer(){
        Debug.print("Démarrage du GameServer...");
        try{
            _server = new ServerSocket(Jelly._config.GAME_PORT);
        }catch(Exception e){
            
        }
    }

    @Override
    public void run(){
        Debug.print("GameServer : A l'écoute du port "+Jelly._config.GAME_PORT);
        while(!isInterrupted()){
            try{
                _clients.add(new GameThread(_server.accept()));
            }catch(Exception e){
                
            }
        }
    }
}
