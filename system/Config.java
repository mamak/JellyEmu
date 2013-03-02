package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import jelly.Jelly;

/**
 *
 * @author vincent
 */
public class Config {
    //database
    public String DB_HOST="";
    public String DB_USER="";
    public String DB_PASS="";
    public String DB_NAME="";

    //preload
    public boolean PRELOAD_MAPS=true;
    public boolean PRELOAD_ACCOUNTS=false;

    //optimisations
    public boolean FREE_UNUSED_ACCOUNTS=true;

    //Configuration serveur
    public int REALM_PORT=445;
    public String GAME_IP="127.0.0.1";
    public int GAME_PORT=5555;
    
    public Config(){
        load();
    }
    
    public void load(){
        Debug.print("Lecture de la configuration");
        try{
            File f=new File("jelly.conf");

            if(!f.exists())
                create(f);
            BufferedReader file = new BufferedReader(new FileReader(f));
            String line;
            String param;
            String value;
            String[] data;
            while((line=file.readLine())!=null)
            {
                data=line.split("=");
                if(data.length<2)
                    continue;
                
                param=data[0].trim().toUpperCase();
                value=data[1].trim();
                
                switch(param)
                {
                    //database
                    case "DB_HOST":
                        DB_HOST=value;
                        break;
                    case "DB_USER":
                        DB_USER=value;
                        break;
                    case "DB_PASS":
                        DB_PASS=value;
                        break;
                    case "DB_NAME":
                        DB_NAME=value;
                        break;
                    //preload
                    case "PRELOAD_MAPS":
                        PRELOAD_MAPS=getBool(value);
                        break;
                    case "PRELOAD_ACCOUNTS":
                        PRELOAD_ACCOUNTS=getBool(value);
                        break;
                    //optimisations
                    case "FREE_UNUSED_ACCOUNTS":
                        FREE_UNUSED_ACCOUNTS=getBool(value);
                        break;
                    //configuration serveur
                    case "REALM_PORT":
                        REALM_PORT=Integer.parseInt(value);
                        break;
                    case "GAME_IP":
                        GAME_IP=value;
                        break;
                    case "GAME_PORT":
                        GAME_PORT=Integer.parseInt(value);
                        break;
                    default:
                        continue;
                }
            }
            Debug.print("Fichier de configuration chargé !");
        }catch(Exception e){
            Debug.error("Chargement de la configuration impossible : "+e.getMessage());
            Jelly.turnoff();
        }
    }

    private boolean getBool(String value){
        value=value.toLowerCase();
        if(value.equals("true")||value.equals("oui"))
            return true;

        return false;
    }

    protected void create(File file){
        Debug.print("Création du fichier de configuration...");
        try{
            file.createNewFile();
            file.setReadable(true, false);
            file.setWritable(true, false);
            if(!file.canWrite())
            {
                Debug.error("Config : impossible d'écrire le fichier de configuration !");
                Jelly.turnoff();
            }

            FileWriter FW = new FileWriter(file);
            FW.write(
                    "#####################################\n" +
                    "# Fichier de configuration de Jelly #\n" +
                    "# By v4vx                           #\n" +
                    "#                       Version 0.1 #\n" +
                    "#####################################\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "#############################\n" +
                    "###Configuration de la bdd###\n" +
                    "#############################\n" +
                    "\n" +
                    "#IP de la base de données\n" +
                    "DB_HOST=127.0.0.1\n" +
                    "\n" +
                    "#Utilisateur\n" +
                    "DB_USER=root\n" +
                    "\n" +
                    "#mot de passe\n" +
                    "DB_PASS=\n" +
                    "\n" +
                    "#Nom de la bdd\n" +
                    "DB_NAME=jelly\n" +
                    "\n" +
                    "\n" +
                    "##############################\n" +
                    "###Configuration du serveur###\n" +
                    "##############################\n" +
                    "\n" +
                    "#Port du serveur de connexion (à bien noter dans le config.xml de dofus)\n" +
                    "REALM_PORT=444\n" +
                    "#Ip de jeu, la même que dans config.xml\n" +
                    "GAME_IP=127.0.0.1\n" +
                    "#Port de jeu (quelconque, mais inutilisé !)\n" +
                    "GAME_PORT=5555\n" +
                    "\n\n" +
                    "########################\n" +
                    "###Performances & co.###\n" +
                    "########################\n" +
                    "\n" +
                    "PRELOAD_MAPS=false\n" +
                    "PRELOAD_ACCOUNTS=false\n" +
                    "FREE_UNUSED_ACCOUNTS=true"
            );
            FW.flush();
            FW.close();

            Debug.print("Configuration généré avec succès !");
        }catch(Exception e){
            Debug.error("Config : Création du fichier de configuration impossible");
            Debug.error("Informations : "+e.getMessage());
            Jelly.turnoff();
        }
    }
}
