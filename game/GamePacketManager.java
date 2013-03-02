package game;

import java.util.Map.Entry;
import jelly.Jelly;
import system.Debug;

public class GamePacketManager {
    protected GameThread _game;

    public GamePacketManager(GameThread game){
        _game=game;
    }

    public void parsePacket(String packet){
        switch(packet.charAt(0)){
            //packet de comptes
            case 'A':
                parseAccountPacket(packet);
                break;
        }
    }

    public void parseAccountPacket(String packet){
        switch(packet.charAt(1)){
            //packet de connexion au GameThread
            case 'T':
                connect(Integer.parseInt(packet.substring(2)));
                break;
            case 'V':
                _game.sendPacket("AV0");
                break;
            //liste des persos
            case 'L':
                sendCharList();
                break;
            //packet de queue
            case 'f':
                _game.sendPacket("Af1|0|1|1|1");
                break;
            //récupération de la clé client
            case 'i':
                _game.clientkey=packet.substring(2);
                break;
            //généaration d'un nom aléatoire
            case 'P':
                _game.sendPacket("APK"+models.Character.generateName());
                break;
            //création d'un perso
            case 'A':
                createPerso(packet);
                break;
            //sélection du personnage (jeu)
            case 'S':
                _game.sendPacket("ASE");
                break;
        }
    }

    public void connect(int id){
        Debug.info("GamePacket : connexion du compte n°"+id);
        _game._account = Jelly._world.getLoggedAccount(id);

        //si le compte n'est pas connecté
        if(_game._account==null){
            _game.sendPacket("ATE");
        }else{
            _game.sendPacket("ATK0");
        }
    }

    public void createPerso(String packet){
        String[] data = packet.substring(2).split("\\|");
        int state = models.Character.create(data[0], Integer.parseInt(data[1]), -1, -1, -1, _game._account.getId());

        //nom existe déjà
        if(state==1){
            _game.sendPacket("AAEa");
            return;
        }else if(state==2){ //compte complet
            _game.sendPacket("AAEf");
            return;
        }

        //création ok
        _game.sendPacket("AAK");

        //charge le personnage
        _game._account.addChar(models.Character.load(data[0]));

        //liste des personnages
        sendCharList();
    }

    public void sendCharList(){
        String packet = "ALK31536000000|"+_game._account.charCount();

        for(Entry<Integer, models.Character> ent : _game._account.getChars().entrySet()){
            packet+="|"+ent.getValue().toString();
        }

        _game.sendPacket(packet);
    }
}
