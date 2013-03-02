
package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jelly.Jelly;

public class Database {    
    protected Connection _db;

    public Database(){
        try{
            String dsn = "jdbc:mysql://"+Jelly._config.DB_HOST;
            dsn+="/"+Jelly._config.DB_NAME;
            _db=DriverManager.getConnection(
                    dsn, 
                    Jelly._config.DB_USER, 
                    Jelly._config.DB_PASS
            );
            Debug.print("Database : Connexion ok !");
        }catch(Exception e)
        {
            String[] config_vars = {
                "Host : "+Jelly._config.DB_HOST,
                "User : "+Jelly._config.DB_USER,
                "Pass : "+Jelly._config.DB_PASS,
                "Dbname : "+Jelly._config.DB_NAME
            };
            Debug.databaseError(e, "Connexion à la bdd impossible.", config_vars);
            Jelly.turnoff();
        }
    }
    
    public synchronized ResultSet query(String query){
        try{
            Statement stmt=_db.createStatement();
            Debug.info("Database : exécutant la requête '"+query+"'...");
            return stmt.executeQuery(query);
        }catch(Exception e){
            Debug.databaseError(e, query);
        }
        
        return null;
    }
    
    public synchronized PreparedStatement prepare(String query){
        try{
            Debug.info("Database : préparant la requête '"+query+"'...");
            return _db.prepareStatement(query);
        }catch(Exception e)
        {
            Debug.databaseError(e, query);
        }
        
        return null;
    }
}
