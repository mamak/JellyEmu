package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import jelly.Jelly;
import system.Debug;

public class Config {
    private static final Config self = new Config();

    private final HashMap<String, String> configData = new HashMap<>();

    public static String getString(String param){
        return self.configData.get(param);
    }

    public static int getInt(String param, int min, int max){
        String value = self.configData.get(param);
        int r = Integer.parseInt(value);

        if(r<min){
            r=min;
        }else if(r>max){
            r=max;
        }

        return r;
    }

    public static int getInt(String param){
        return getInt(param, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int getInt(String param, int min){
        return getInt(param, Integer.MIN_VALUE);
    }

    public static boolean getBool(String param){
        String value = self.configData.get(param);

        switch(value){
            case "0":
            case "off":
            case "no":
            case "false":
            case "non":
                return false;
            case "1":
            case "on":
            case "yes":
            case "true":
            case "oui":
                return true;
            default:
                return false;
        }
    }

    private Config(){
        File f = new File(jelly.Constants.CONFIG_FILE);

        if(!f.exists()){
            create(f);
        }
        
        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            String line;
            String param;
            String value;

            while((line=file.readLine())!=null){
                param = line.split("=")[0].toLowerCase().trim();
                value = line.split("=")[1].toLowerCase().trim();

                configData.put(param, value);
            }
        } catch (Exception ex) {}
    }

    private void create(File file){
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
