package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TreeMap;
import jelly.Jelly;
import system.*;

public class Account {
    protected int id;
    protected String account;
    protected String pass;
    protected String pseudo;
    protected int level;
    protected String answer;
    protected String response;

    protected realm.RealmThread _thread=null;

    protected java.util.Map<Integer, Character> _characters;
    
    public Account(int id, String account, String pass, String pseudo, int level, String answer, String response){
        this.account=account;
        this.id=id;
        this.pass=pass;
        this.pseudo=pseudo;
        this.level=level;
        this.answer=answer;
        this.response=response;

        _characters = Character.loadByAccount(id);
    }

    /*************************************
     *            Partie SQL             *
     *************************************/
    
    /**
     * Load an account
     * @param account
     * @return 
     */
    public static Account load(String account){
        try{
            PreparedStatement stmt = Jelly._database.prepare("SELECT * FROM accounts where account = ?");
            stmt.setString(1, account);
            ResultSet RS = stmt.executeQuery();
            if(!RS.next())
                return null;
            return new Account(
                    RS.getInt("id"),
                    RS.getString("account"),
                    RS.getString("pass"),
                    RS.getString("pseudo"),
                    RS.getInt("level"),
                    RS.getString("answer"),
                    RS.getString("response")
            );
        }catch(Exception e){
            String[] args = {
                "Account : "+account
            };
            Debug.databaseError(e, "Chargement du compte impossible", args);
            return null;
        }
    }
    
    /**
     * Load an account with id
     * @param id
     * @return 
     */
    public static Account load(int id){
        try{
            PreparedStatement stmt = Jelly._database.prepare("SELECT * FROM accounts where id = ?");
            stmt.setInt(1, id);
            ResultSet RS = stmt.executeQuery();
            if(!RS.next())
                return null;
            return new Account(
                    RS.getInt("id"),
                    RS.getString("account"),
                    RS.getString("pass"),
                    RS.getString("pseudo"),
                    RS.getInt("level"),
                    RS.getString("answer"),
                    RS.getString("response")
            );
        }catch(Exception e){
            String[] args = {
                "id : "+id
            };
            Debug.databaseError(e, "Chargement du compte impossible", args);
            return null;
        }       
    }
    
    /**
     * Load all accounts
     * @return
     * @throws Exception 
     */
    public static ArrayList getAll() throws Exception
    {
        ArrayList accounts = new ArrayList();
        ResultSet RS = Jelly._database.query("SELECT * FROM accounts");
        int id;
        String account;
        String pass;
        String pseudo;
        int level;
        String answer;
        String response;
        while(RS.next()){
            try{
                id=RS.getInt("id");
                account=RS.getString("account");
                pass=RS.getString("pass");
                pseudo=RS.getString("pseudo");
                level=RS.getInt("level");
                answer=RS.getString("answer");
                response=RS.getString("response");
                accounts.add(new Account(id, account, pass, pseudo, level, answer, response));
            }catch(Exception e){
                Debug.error("Account : "+e.getMessage());
            }
        }
        
        return accounts;
    }

    /**
     * Sauvegarde le compte
     */
    public void save(){
        long lastLogin = System.currentTimeMillis();
        try{
            PreparedStatement _stmt = Jelly._database.prepare("UPDATE accounts SET lastLogin = ? WHERE id = ?");          
            _stmt.setLong(1, lastLogin);
            _stmt.setInt(2, id);
            _stmt.executeUpdate();
            Debug.info("Account : compte n°"+id+" sauvegardé avec succès !");
        }catch(Exception e){
            String[] vars = {
                "lastLogin : "+lastLogin,
                "id : "+id
            };
            Debug.databaseError(e, "Account - Sauvegarde du compte impossible !", vars);
        }
    }

    /*
     * getter / setters
     */

    //compte
    
    public int getId(){
        return id; 
    }
    
    public String getAccount(){
        return account;
    }
    
    public String getPseudo(){
        return pseudo;
    }

    public String getAnswer(){
        return answer;
    }

    public String getResponse(){
        return response;
    }

    public int getLevel(){
        return level;
    }

    //personnages

    public int charCount(){
        return _characters.size();
    }

    public void addChar(Character _char){
        _characters.put(_char.getId(), _char);
    }

    public java.util.Map<Integer, Character> getChars(){
        return _characters;
    }

    //thread

    public void setThread(realm.RealmThread thread){
        _thread=thread;
    }

    public realm.RealmThread getThread(){
        return _thread;
    }

    public static Account login(String user, String pass){
        Account _acc = Jelly._world.getAccount(user);

        if(_acc==null){
            Debug.info("Account : compte '"+user+"' inexistant !");;
            return null;
        }
        
        if(!_acc.passValid(pass)){
            Debug.info("Account : mot de passe incorrecte pour le compte '"+user+"'");
            return null;
        }

        return _acc;
    }

    /**
     * Dissocie le compte du thread et detruit le thread
     */
    public void logout(){
        _thread.close();
        _thread=null;
        Jelly._world.deleteLoggedAccount(id);
    }

    public boolean passValid(String password){
        return this.pass.equals(password);
    }
}
