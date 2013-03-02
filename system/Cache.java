package system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import system.*;

public class Cache {
    public static void save(String filename, Object data){
        try{
            if(!(data instanceof java.io.Serializable))
            {
                Debug.error("Cache : la classe '"+data+"' n'est pas linéarisable !");
                return;
            }
            
            Debug.info("Cache : essaye d'enregistrer '"+filename+"'...");
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(file);

            oos.writeObject(data);
            oos.flush();
            oos.close();
            
            Debug.info("Chache : fichier sauvegardé '"+filename+"' avec succès !");
        }catch(Exception e){
            Debug.error("Cache : impossible d'enregistrer "+filename);
            Debug.error("Informations : "+e.getMessage());
        }
    }

    public static Object load(String filename){
        try{
            Debug.info("Cache : tente de récupérer '"+filename+'"');
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(file);
            return ois.readObject();
        }catch(Exception e){
            Debug.error("Cache : récupération impossible de '"+filename+"'");
            Debug.error("Informations : "+e.getMessage());
            return null;
        }
    }
}
