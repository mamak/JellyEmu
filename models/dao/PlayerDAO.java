package models.dao;

import core.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Player;

public class PlayerDAO extends core.database.DAO<Player> {
    private PreparedStatement findByNameStatement = null;

    @Override
    protected String tableName(){
        return "characters";
    }

    @Override
    protected Player createByResultSet(ResultSet RS){
        try{
            Player p = new Player();

            p.id = RS.getInt("id");
            p.name = RS.getString("name");
            p.accountId = RS.getInt("account");
            p.classId = RS.getByte("class");
            p.color1 = RS.getByte("color1");
            p.color2 = RS.getByte("color2");
            p.color3 = RS.getByte("color3");
            p.gfxid = RS.getInt("gfxid");
            p.sexe = RS.getByte("sexe");
            p.level = RS.getInt("level");

            return p;
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public boolean create(Player p){
        return false;
    }

    @Override
    public boolean update(Player P){
        return false;
    }

    public Player findByName(String name){
        try {
            if(findByNameStatement == null){
                findByNameStatement = Database.prepare("SELECT * FROM characters WHERE name = ?");
            }

            findByNameStatement.setString(1, name);
            ResultSet RS = findByNameStatement.executeQuery();

            if(!RS.next()){
                return null;
            }

            return createByResultSet(RS);
        } catch (SQLException ex) {
            return null;
        }
    }
}
