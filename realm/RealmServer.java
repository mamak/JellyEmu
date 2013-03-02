package realm;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import jelly.Jelly;
import system.*;

public class RealmServer extends Thread {
    protected ServerSocket _socketServer;
    public boolean ready = false;
    protected ArrayList<RealmThread> _clients = new ArrayList<>();

    public RealmServer(){
        try{
            Debug.print("Realm : démarrage du serveur...");
            _socketServer = new ServerSocket(Jelly._config.REALM_PORT);
        }catch(Exception e){
            Debug.error("Realm : Erreur lors de l'initialisation du serveur");
            Debug.error("Informations : "+e.getMessage());
            Jelly.turnoff();
        }
    }

    public void closeServer(){
        try{
            Debug.print("Realm : arrêt du serveur...");
            kickAll();
            ready=false;
            _socketServer.close();
        }catch(Exception e){
            Debug.error("Realm : Impossible d'arrêter le serveur");
            Debug.error("Informations : "+e.getMessage());
        }
    }

    public void kickAll(){
        for(RealmThread r : _clients){
            r.close();
        }
        Debug.info("RealmServer : "+_clients.size()+" connexion ont été détruites !");

        _clients.clear();
    }

    public void run(){
        Debug.print("Realm : serveur démarré sur le port "+Jelly._config.REALM_PORT+" !");
        ready=true;
        while(ready){
            try{
                //soulage un peu le processeur (on est pas à quelques ms près ;)
                Thread.sleep(10);
                _clients.add(new RealmThread(_socketServer.accept()));
            }catch(Exception e){
                Debug.error("RealmServer : problème lors de la connection d'un client");
                Debug.error("Informations : "+e.getMessage());
            }
        }
    }

    public void delete(int id){
        Debug.info("RealmServer : Supprimant RealmThread n°"+id+"...");
        _clients.get(id).close();
        _clients.remove(id);
        System.gc();
        Debug.info("RealmServer : RealmThread n°"+id+" supprimé !");
    }

    public void delete(RealmThread client){
        Debug.info("RealmServer : Suppression d'un RealmThread anonyme...");
        int id = _clients.indexOf(client);
        delete(id);
    }
}
