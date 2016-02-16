package org.tianzx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by tianzx on 2016/2/15.
 */
public class SelectorServerChannelDemo {

    public static void startServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(8899));
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            if(select>0) {
                for (SelectionKey key :selector.selectedKeys()) {
                    if (key.isAcceptable()) {
//                        key.channel().accept();

//                        SocketChannel socketChannel =  ((SocketChannel)key.channel()).accept();

                        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
                        ByteBuffer buffer = ByteBuffer.allocate(48);
                        int size = socketChannel.read(buffer);
                        while(size>0){
                            buffer.flip();
                            Charset charset = Charset.forName("UTF-8");
                            System.err.println(charset.newDecoder().decode(buffer).toString());
                            size = socketChannel.read(buffer);
                        }
                        buffer.clear();

                        ByteBuffer response = ByteBuffer.wrap("已接受请求".getBytes("UTF-8"));
                        socketChannel.write(response);
                        socketChannel.close();
                        selector.selectedKeys().remove(key);
                    }
                }
            }
        }
    }

    public static void main(String []args) {
        try {
            SelectorServerChannelDemo.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
