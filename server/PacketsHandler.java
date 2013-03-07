package server;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import jelly.Jelly;

public class PacketsHandler implements Runnable {
    public static final PacketsHandler instance = new PacketsHandler();
    private Thread t;

    private final HashMap<SelectionKey, String> pendingPackets = new HashMap<>();

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
                    pendingPackets.put(key, packet.trim());
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

                Iterator<Entry<SelectionKey, String>> iterator = pendingPackets.entrySet().iterator();

                while(iterator.hasNext()){
                    Entry ent = iterator.next();
                    iterator.remove();

                    processPacket((SelectionKey)ent.getKey(), (String)ent.getValue());
                }

                pendingPackets.clear();
            }
        }
    }

    private void processPacket(SelectionKey key, String packet){
        switch(packet.charAt(0)){
            //accounts
            case 'A':
                break;
        }
    }
}
