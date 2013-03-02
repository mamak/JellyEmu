package game;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import jelly.Jelly;
import models.Account;
import system.*;

public final class World {
    protected Map<Integer, models.Account> _accounts = new TreeMap<>();
    protected Map<String, Integer> _accountsId = new TreeMap<>();
    protected Map<Integer, models.Map> _maps = new TreeMap<>();
    protected Map<Integer, models.Account> _logged = new TreeMap<>();
    
    public World(){
        Debug.print("World : Création du monde...");

        if(Jelly._config.PRELOAD_ACCOUNTS)
            loadAccounts();
        if(Jelly._config.PRELOAD_MAPS)
            loadMaps();
    }

    public void loadMaps(){
        Debug.print("World : Chargement des maps...");
        _maps=models.Map.getAll();
        if(_maps==null)
            Jelly.turnoff();
        Debug.print("World : "+_maps.size()+" maps chargées");
    }
    
    /**
     * Charge tout les comptes
     */
    public void loadAccounts(){
        Debug.print("World : Chargement des comptes");
        try{
            ArrayList accounts=models.Account.getAll();
            int size = accounts.size();
            models.Account account;
            for(int i = 0; i < size; i++)
            {
                account = (Account)accounts.get(i);
                _accounts.put(account.getId(), account);
                _accountsId.put(account.getAccount(), account.getId());
            }
        }catch(Exception e){
            Debug.error("World : Chargement des comptes impossible !");
            Debug.error(e.getMessage(), true);
            Jelly.turnoff();
        }
        Debug.print("World : "+_accounts.size()+" comptes chargés");
    }

    /**
     * Get an account with the id
     * @param id
     * @return
     */
    public models.Account getAccount(int id){        
        models.Account account;

        if(!_accounts.containsKey(id))
        {
            account=models.Account.load(id);
            if(account==null)
                return null;
            _accounts.put(id, account);
        }else
            account = _accounts.get(id);

        return account;
    }

    public models.Account getAccount(String name){
            System.out.print("ok");
        if(_accountsId.containsKey(name))
            return getAccount(_accountsId.get(name));

         models.Account account=models.Account.load(name);
         
         if(account==null)
            return null;

         _accounts.put(account.getId(), account);

         return account;
    }

    public void freeAccount(int id){
        Debug.print("Libération du compte n°"+id);
        _accounts.remove(id);
    }

    public void freeAccount(String name){
        int id = getAccountId(name);
        freeAccount(id);
    }

    public int getAccountId(String name){
        return _accountsId.get(name);
    }

    public void addLoggedAccount(int id, models.Account account){
        _logged.put(id, account);
    }

    public models.Account getLoggedAccount(int id){
        return _logged.get(id);
    }

    public void deleteLoggedAccount(int id){
        _logged.remove(id);
    }
}
