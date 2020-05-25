package cn.maddie.androidtcpserver;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class TCPServer {
    Socket socket;
    ServerSocket serverSocket;
    InputStream inputStream;
    OutputStream outputStream;
    Message message;
    Handler handler;

    public TCPServer(Handler handler) {
        this.handler = handler;
    }

    public void conntcp( int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("?????????????");
        while (true){
            socket = serverSocket.accept();
            if (socket != null){
                System.out.println("连接成功");
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                message = new Message();
                message.what = 3;//连接成功
                handler.sendMessage(message);
                accept();
            }
        }
    }

    private void accept() throws IOException {
        while (true) {
            byte[] byte1 = new byte[1024];
            int i = inputStream.read(byte1);
            byte[] byte2 = new byte[i];
            System.arraycopy(byte1, 0, byte2, 0, i);
            String s = new String(byte2, "UTF-8");
            message = new Message();
            message.what = 1;//接收
            message.obj = s;
            handler.sendMessage(message);
        }
    }

    public void send(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] bytes = s.getBytes();
                    outputStream.flush();
                    outputStream.write(bytes);
                    message = new Message();
                    message.what = 2;//发送
                    message.obj = s;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
