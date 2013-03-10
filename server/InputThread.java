package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import jelly.Jelly;

public abstract class InputThread implements Runnable {
    protected ServerSocketChannel server;
    protected Selector selector;
    protected Thread t;
    public final OutputThread _out = new OutputThread();

    protected final HashMap<SelectionKey, String> uncompletePackets = new HashMap<>();

    public InputThread(int port){
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

                    try{
                        if(!key.isValid()){
                            continue;
                        }else if(key.isAcceptable()){
                            accept();
                        }else if(key.isReadable()){
                            read(key);
                        }
                    }catch(IOException e){
                        if(key.channel() instanceof SocketChannel){
                            close(key);
                        }else{
                            //shutdown
                        }
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

    private void read(SelectionKey key) throws IOException{
        SocketChannel sock = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(32);

        int state = sock.read(buffer);

        if(state==-1){
            close(key);
            return;
        }

        String packet = new String(buffer.array());
        String lastUncompletePacket = uncompletePackets.get(key);

        if(lastUncompletePacket!=null){
            packet = lastUncompletePacket + packet;
        }

        if(isCompletePacket(packet)){
            uncompletePackets.remove(key);
            onReadAction(key, packet);
        }else{
            uncompletePackets.put(key, packet);
        }
    }

    private boolean isCompletePacket(String packet){
        char last = packet.charAt(packet.length()-1);

        return last == '\u0000' || last == '\n' || last == '\r';
    }

    public void close(SelectionKey key) throws IOException{
        onCloseAction(key);
        ((SocketChannel)key.channel()).close();
        key.cancel();
    }

    protected abstract void onReadAction(SelectionKey key, String packet);
    protected abstract void onConnectAction(SelectionKey key);
    protected abstract void onCloseAction(SelectionKey key);
}
