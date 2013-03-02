package realm;

import jelly.*;
import system.Debug;

public class PacketManager {
    protected realm.RealmThread _realm;

    public PacketManager(realm.RealmThread realm){
        _realm=realm;
    }

    public void parsePacket(String packet){
        switch(_realm.getCount()){
            //vérification de la version du client
            case 1 :
                break;
            //récupération du login rentré
            case 2 :
                _realm.setAccountName(packet);
                break;
            //récupération du pass crypté
            case 3 :
                if(!packet.startsWith("#1")) {
                    Jelly._realm.delete(_realm);
                    return;
                }
                if(!_realm.login(decryptPass(packet))){
                    //Fake compte ou mauvais mdp
                    Debug.info("Login a échoué !");
                    _realm.sendPacket("AlEf");
                    Jelly._realm.delete(_realm);
                }else{
                    //Compte Ok
                    sendLoginPackets();
                }
                break;
            default:
                switch(packet.substring(0, 2)){
                    case "Af":
                        //système de queue (non fonctionnel)
                        _realm.sendPacket("Af1|0|1|1|1");
                        break;
                    //liste des serveurs + nb persos
                    case "Ax":
                        sendCharactersList();
                        break;
                    //IP du serveur de jeu
                    case "AX":
                        _realm.sendPacket("AYK"+Jelly._config.GAME_IP+":"+Jelly._config.GAME_PORT+";"+_realm.getAccount().getId());
                        break;
                }
        }
    }

    public String decryptPass(String packet){
        packet=packet.substring(2);
        int l1, l2, l3, l4, l5;
        String l7 = "";
        String key = _realm.getHashKey();
        int length = packet.length();
        String Chaine = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        for(l1=0;l1<length;l1+=2)
        {
            l3 = (int)key.charAt((l1/2));
            l2 = Chaine.indexOf(packet.charAt(l1));
            l4 = (64 + l2) - l3;
            int l11 = l1+1;
            l2 = Chaine.indexOf(packet.charAt(l11));
            l5 = (64 + l2) - l3;
            if(l5 < 0)
                l5 = 64 + l5;

            l7 = l7 + (char)(16 * l4 + l5);
        }
        return l7;
    }

    public void sendCharactersList(){
        //AxK[temps d'abonnement]|[server id],[char count]
        String packet = "AxK31536000000|1,"+_realm.getAccount().charCount();

        _realm.sendPacket(packet);
    }

    public void sendLoginPackets(){
        //si n'est pas connecté (ou déjà connecté) : détruit le thread
        if(!_realm.logged){
            _realm.close();
            return;
        }

        //pseudo
        _realm.sendPacket("Ad"+_realm.getAccount().getPseudo());
        //???
        _realm.sendPacket("Ac0");
        //statut serveur : [id];[statut];[niveau de remplissage];[disponible]
        _realm.sendPacket("AH1;1;110;1");
        //niveau GM (0 : joueur, 1 : admin/modo...)
        _realm.sendPacket("AlK"+(_realm.getAccount().getLevel()>0?"1":"0"));
        //question (les espaces sont des +)
        _realm.sendPacket("AQ"+_realm.getAccount().getAnswer().replace(" ", "+"));
    }
}
