package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class Client {
    protected SelectionKey key;
    protected InputThread server;
    protected SocketChannel sock;

    protected Client(SelectionKey key, InputThread server){
        this.key=key;
        this.server=server;

        sock=(SocketChannel)key.channel();
    }

    public void close(){
        try {
            server.close(key);
        } catch (IOException ex) {}
    }

    public final void write(String packet) throws IOException{
        packet+=(char)0x00;
        sock.write(ByteBuffer.wrap(packet.getBytes()));
    }

    public final void send(String packet){
        server._out.send(this, packet);
    }

    public final SelectionKey getKey(){
        return key;
    }

    public final SocketChannel getSocket(){
        return sock;
    }

    public final InputThread getServer(){
        return server;
    }
}
