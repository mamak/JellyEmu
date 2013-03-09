package server;

import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
import jelly.Jelly;

public class PacketsHandler implements Runnable {
    public static final PacketsHandler instance = new PacketsHandler();
    private Thread t;

    private final Queue<PacketData> pendingPackets = new SynchronousQueue<>();

    private PacketsHandler(){
        t = new Thread(this);
        t.start();
    }

    public void add(SelectionKey key, String packetData){
        synchronized(pendingPackets){
            String[] packets = packetData.split("\n|\u0000|\r");

            for(String packet : packets){
                if(packet.trim().isEmpty()){
                    continue;
                }else{
                    pendingPackets.add(new PacketData(key, packet));
                }
            }

            pendingPackets.notify();
        }
    }

    @Override
    public void run(){
        while(Jelly.running && !t.isInterrupted()){
            synchronized(pendingPackets){
                while(pendingPackets.isEmpty()){
                    try {
                        pendingPackets.wait();
                    } catch (InterruptedException ex) {}
                }
            }

            PacketData packet;

            synchronized(pendingPackets){
                packet = pendingPackets.poll();
            }

            processPacket(packet.key, packet.packet);
        }
    }

    private void processPacket(SelectionKey key, String packet){
        switch(packet.charAt(0)){
            //accounts
            case 'A':
                processAccountPacket(key, packet);
                break;
        }
    }

    private void processAccountPacket(SelectionKey key, String packet){
        
    }

    private class PacketData{
        public SelectionKey key;
        public String packet;

        public PacketData(SelectionKey key, String packet){
            this.key=key;
            this.packet=packet;
        }
    }
}
