package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import system.Debug;

public class GameThread implements Runnable{
    protected Socket _socket;
    protected Thread _thread;
    protected BufferedReader _input;
    protected PrintWriter _output;
    protected GamePacketManager _manager;
    public String clientkey;
    public models.Account _account;

    public GameThread(Socket sock){
        Debug.info("GameThread : création d'un nouveau thread...");
        try{
            _socket = sock;
            _input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _output = new PrintWriter(_socket.getOutputStream());
            _manager = new GamePacketManager(this);
        }catch(Exception e){
            
        }
        _thread = new Thread(this);
        _thread.start();
    }

    @Override
    public void run(){
        Debug.info("GameThread : Thread lancée !");;
        //packet de confirmation de connexion
        sendPacket("HG");
        String packet = "";
        char[] buffer = new char[1];
        try{
            while(_input.read(buffer, 0, 1)!=-1 && !_thread.isInterrupted()){
                if(buffer[0]!='\u0000' && buffer[0]!='\n' && buffer[0] != '\r'){
                    packet+=buffer[0];
                }else if(!packet.isEmpty()){
                    Debug.info("GameThread : packet reçut '"+packet+"' !");
                    _manager.parsePacket(packet);
                    packet="";
                }
            }
        }catch(Exception e){

        }
    }

    public void sendPacket(String packet){
        _output.print(packet+(char)0x00);
        _output.flush();
        Debug.info("GameThread : envoit du packet '"+packet+"' réussit !");
    }
}
