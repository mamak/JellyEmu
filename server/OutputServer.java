package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import jelly.Jelly;

public class OutputServer extends Thread {
    private final ArrayList<Request> pendingList = new ArrayList<>();

    public void send(SelectionKey key, String packet){
        packet+=(char)0x00;

        synchronized(pendingList){
            pendingList.add(new Request(key, packet));
            pendingList.notify();
        }
    }

    @Override
    public void run(){
        while(Jelly.running && !this.isInterrupted()){
            synchronized(pendingList){
                while(pendingList.isEmpty()){
                    try {
                        pendingList.wait();
                    } catch (InterruptedException ex) {}
                }

                Iterator<Request> iterator = pendingList.iterator();

                while(iterator.hasNext()){
                    Request req = iterator.next();
                    iterator.remove();

                    try {
                        write(req.key, req.getBytes());
                    } catch (IOException ex) {}
                }

                pendingList.clear();
            }
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
