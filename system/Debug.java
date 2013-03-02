package system;

import jelly.Commands.*;
import jelly.Jelly;

public class Debug {  
    /**
     * Affiche un message et l'enregistre dans les log si voulut
     * @param msg 
     */
    public static void print(String msg){
        if(Console.active)
            Console.prepareDebugLine();
        System.out.println(msg);
        if(Console.active)
            Console.newLine();
    }

    public static void info(String msg){
        if(!Jelly.DEBUG)
            return;
        if(Console.active)
            Console.prepareDebugLine();
        System.out.println("Debug : "+msg);
        if(Console.active)
            Console.newLine();
    }
    
    /**
     * Affiche une erreur
     * @param msg 
     */
    public static void error(String msg){
        if(Console.active)
            Console.prepareDebugLine();
        System.err.println(msg);
        if(Console.active)
            Console.newLine();
    }
    
    /**
     * Affiche une erreur et indente le message
     * @param msg
     * @param ind 
     */
    public static void error(String msg, boolean ind){
        if(ind)
            error('\t'+msg);
        else
            error(msg);
    }
    
    /**
     * Affiche une liste d'erreur, possiblement indentés
     * @param msgs
     * @param ind 
     */
    public static void error(String[] msgs, boolean ind){
        int size = msgs.length;
        for(int i=0;i<size;i++)
        {
            Debug.error(msgs[i], ind);
        }
    }
    
    /**
     * Erreur de la base de donnée
     * @param e
     * @param msg 
     */
    public static void databaseError(Exception e, String msg){
        error("Database error : "+msg);
        error("Informations : "+e.getMessage());
    }
    
    /**
     * Erreur de la base de donnée
     * @param e
     * @param msgs 
     */
    public static void databaseError(Exception e, String msg, String[] vars){
        error("Database error : "+msg);
        error("Informations : "+e.getMessage());
        error(vars, true);
    }
    
    public static void init_log(){
        
    }
}
