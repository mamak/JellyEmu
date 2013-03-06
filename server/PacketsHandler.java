package server;

import jelly.Jelly;

public class PacketsHandler implements Runnable {
    public static PacketsHandler instance;

    private Thread t;

    public PacketsHandler(){
        t = new Thread(this);
        t.start();

        instance = this;
    }

    @Override
    public void run(){
        while(Jelly.running && !t.isInterrupted()){
            
        }
    }
}
