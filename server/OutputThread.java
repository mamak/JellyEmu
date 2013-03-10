package server;

import core.utils.PendingList;
import java.io.IOException;
import jelly.Jelly;

public class OutputThread extends Thread {
    private final PendingList<Request> pendingList = new PendingList<>();

    public void send(Client client, String packet){
        packet+=(char)0x00;
        pendingList.add(new Request(client, packet));
    }

    @Override
    public void run(){
        while(Jelly.running && !this.isInterrupted()){
            try {
                pendingList.waitForElements();
            } catch (InterruptedException ex) {}

            Request req = pendingList.pop();

            try {
                req.client.write(req.packet);
            } catch (IOException ex) {}
        }
    }

    private class Request{
        public String packet;
        public Client client;

        public Request(Client client, String packet){
            this.client=client;
            this.packet=packet;
        }
    }
}
