package realm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import jelly.Jelly;
import system.Debug;

public class RealmThread implements Runnable{
    protected Thread _thread;
    protected Socket _socket;
    protected BufferedReader _input;
    protected PrintWriter _output;
    protected String hashkey="";
    protected int count=0;
    protected realm.PacketManager _manager;
    protected String accountName="";
    protected models.Account _account=null;

    //si le thread est lié à un compte
    public boolean logged = false;

    public RealmThread(Socket socket){
        try{
            _socket = socket;
            Debug.info("RealmThread : nouvelle connexion de "+_socket.getInetAddress());
            Debug.info("Création du thread...");
            _input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _output = new PrintWriter(_socket.getOutputStream());
            _manager = new realm.PacketManager(this);
            _thread = new Thread(this);
            _thread.start();
        }catch(Exception e){
            
        }
    }

    @Override
    public void run(){
        try{
            Debug.info("Thread opérationnel !");
            generateHashKey();
            String packet;
            char[] buffer = new char[1];
            packet="";
            while(_input.read(buffer, 0, 1)!=-1 && !_thread.isInterrupted()){
                if(buffer[0]!='\u0000' && buffer[0]!='\n'){
                    packet+=buffer[0];
                }else if(!packet.isEmpty()){
                    Debug.info("Packet reçut : "+packet);
                    count++;
                    _manager.parsePacket(packet);
                    packet="";
                }
            }
        }catch(Exception e){

        }

        close();
    }

    protected void generateHashKey(){
        try{
            Random rand = new Random();
            String letters = "abcdefghijklmnopqrstuvwxyz";
            for(int i=0;i<32;i++){
                hashkey+=letters.charAt(rand.nextInt(26));
            }

            sendPacket("HC"+hashkey);
        }catch(Exception e){
            
        }
    }

    public void sendPacket(String packet){
        try{
            packet=new String(packet.getBytes("UTF8"));
            _output.print(packet+(char)0);
            _output.flush();
            Debug.info("Realm : Packet '"+packet+"' envoyé !");
        }catch(Exception e){
            
        }
    }

    public int getCount(){
        return count;
    }

    public void setAccountName(String name){
        accountName=name;
    }

    public boolean login(String pass){
        Debug.info("RealmThread : Connexion en cours...");
        if(accountName.length()<4)
            return false;
        
        models.Account acc = models.Account.login(accountName, pass);

        if(acc==null)
            return false;

        //si compte déjà lié à un thread, on le déconnecte
        if(acc.getThread()!=null){
            //Met logged à false pour éviter une destruction du compte
            acc.getThread().logged=false;
            acc.logout();
            //envoit le packet "déjà connecté"
            sendPacket("AlEc");
            //compte correcte, mais déjà connecté
            return true;
        }

        acc.setThread(this);
        _account=acc;
        Debug.info("RealmThread : Compte lié au thread !");

        //ajout du compte dans la liste des connectés
        Jelly._world.addLoggedAccount(_account.getId(), _account);

        logged = true;

        return true;
    }

    public void close(){
        Debug.info("RealmThread : Destruction du Thread...");
        try{
            _socket.close();
            if(logged && Jelly._world.getLoggedAccount(_account.getId())!=null){
                //dissocie le thread du compte (impossible d'utiliser logout, car il appelle close())
                _account.setThread(null);
                _account=null;
                //si on veut enlever le compte inutiliser...
                if(Jelly._config.FREE_UNUSED_ACCOUNTS)
                    Jelly._world.freeAccount(accountName);
            }
            logged=false;
            accountName="";
            _manager=null;
            _input=null;
            _output=null;
            _thread.interrupt();
            _thread=null;
        }catch(Exception e){
            
        }
    }

    public String getHashKey(){
        return hashkey;
    }

    public models.Account getAccount(){
        return _account;
    }
}
