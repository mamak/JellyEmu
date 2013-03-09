package core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jelly.Jelly;

public class Database {
    public Connection db;
    private final static Database self = new Database();

    private Database(){
        try {
            StringBuilder dsn = new StringBuilder();

            dsn.append("jdbc:mysql://");
            dsn.append(Jelly._config.DB_HOST);
            dsn.append("/").append(Jelly._config.DB_NAME);
            
            db = DriverManager.getConnection(
                    dsn.toString(),
                    Jelly._config.DB_USER,
                    Jelly._config.DB_PASS
            );
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet query(String query){
        try{
            ResultSet RS;
            synchronized(self){
                RS = self.db.createStatement().executeQuery(query);
            }
            return RS;
        }catch(SQLException e){
            return null;
        }
    }

    public static PreparedStatement prepare(String query){
        try{
            PreparedStatement stmt = self.db.prepareStatement(query);
            return stmt;
        }catch(SQLException e){
            return null;
        }
    }
}
