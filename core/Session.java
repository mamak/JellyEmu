package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {
    private SelectionKey key;
    private boolean login = false;
    private SocketChannel sock;

    public Session(SelectionKey key){
        this.key = key;
        sock = (SocketChannel)key.channel();
    }

    public void send(String packet) throws IOException{
        packet+=(char)0x00;
        sock.write(ByteBuffer.wrap(packet.getBytes()));
    }

    public void logout(boolean socket){
        if(socket){
            try {
                sock.close();
                key.cancel();
            } catch (IOException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public SelectionKey getKey(){
        return key;
    }

    public boolean isLogin(){
        return login;
    }

    public SocketChannel getSocket(){
        return sock;
    }
}
