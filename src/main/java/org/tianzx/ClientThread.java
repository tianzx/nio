package org.tianzx;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by tianzx on 2016/2/16.
 */
public class ClientThread extends Thread{

    public ClientThread(Selector selector) {
        super();
        this.selector = selector;
    }

    private Selector selector;

    @Override
    public void run() {
        try {
            while(selector.select()>0) {
                for(SelectionKey key:selector.selectedKeys()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(48);
                    int size = socketChannel.read(byteBuffer);
                    while(size>0) {
                        Charset charset = Charset.forName("UTF-8");
                        System.err.println(charset.newDecoder().decode(byteBuffer).toString());
                        size = socketChannel.read(byteBuffer);
                    }
                    selector.selectedKeys().remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
