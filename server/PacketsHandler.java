package server;

import core.utils.PendingList;
import jelly.Jelly;

public class PacketsHandler implements Runnable {
    public static final PacketsHandler instance = new PacketsHandler();
    private Thread t;

    private final PendingList<PacketData> pendingPackets = new PendingList<>();

    private PacketsHandler(){
        t = new Thread(this);
        t.start();
    }

    public void add(Client client, String packetData){
        String[] packets = packetData.split("\n|\u0000|\r");

        for(String packet : packets){
            if(packet.trim().isEmpty()){
                continue;
            }else{
                pendingPackets.push(new PacketData(client, packet));
            }
        }
    }

    @Override
    public void run(){
        while(Jelly.running && !t.isInterrupted()){
            try {
                pendingPackets.waitForElements();
            } catch (InterruptedException ex) {}

            PacketData packet = pendingPackets.pop();

            processPacket(packet.client, packet.packet);
        }
    }

    private void processPacket(Client client, String packet){
        switch(packet.charAt(0)){
            //accounts
            case 'A':
                processAccountPacket(client, packet);
                break;
        }
    }

    private void processAccountPacket(Client client, String packet){
        
    }

    private class PacketData{
        public Client client;
        public String packet;

        public PacketData(Client client, String packet){
            this.client=client;
            this.packet=packet;
        }
    }
}
