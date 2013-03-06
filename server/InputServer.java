package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import jelly.Jelly;

public abstract class InputServer implements Runnable {
    protected ServerSocketChannel server;
    protected Selector selector;
    protected Thread t;
    public final OutputServer _out = new OutputServer();

    public InputServer(int port){
        try {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            t = new Thread(this);
            t.start();

            _out.start();
        } catch (IOException ex) {}

    }
    
    @Override
    public void run(){
        while(Jelly.running && !t.isInterrupted()){
            try {
                selector.select();
                
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(!key.isValid()){
                        continue;
                    }else if(key.isAcceptable()){
                        accept();
                    }
                }
            } catch (IOException ex) {}
        }
    }

    private void accept() throws IOException{
        SocketChannel sock = server.accept();
        sock.configureBlocking(false);
        SelectionKey key = sock.register(selector, SelectionKey.OP_READ);
        onConnectAction(key);
    }

    protected abstract void onConnectAction(SelectionKey key);
}
