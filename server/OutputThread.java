package server;

import core.utils.PendingList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import jelly.Jelly;

public class OutputThread extends Thread {
    private final PendingList<Request> pendingList = new PendingList<>();

    public void send(SelectionKey key, String packet){
        packet+=(char)0x00;
        pendingList.add(new Request(key, packet));
    }

    @Override
    public void run(){
        while(Jelly.running && !this.isInterrupted()){
            try {
                pendingList.waitForElements();
            } catch (InterruptedException ex) {}

            Request req = pendingList.pop();

            try {
                write(req.key, req.getBytes());
            } catch (IOException ex) {}
        }
    }

    private void write(SelectionKey key, ByteBuffer data) throws IOException{
        SocketChannel sock = (SocketChannel)key.channel();
        sock.write(data);
    }

    private class Request{
        public String packet;
        public SelectionKey key;

        public Request(SelectionKey key, String packet){
            this.key=key;
            this.packet=packet;
        }

        public ByteBuffer getBytes(){
            return ByteBuffer.wrap(packet.getBytes());
        }
    }
}
