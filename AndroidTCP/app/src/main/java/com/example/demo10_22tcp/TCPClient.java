package com.example.demo10_22tcp;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    Message message;
    Handler handler;

    public TCPClient(Handler handler) {
        this.handler = handler;
    }

    public void conntcp(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        message = new Message();
        message.what = 3;//连接成功
        handler.sendMessage(message);
        accept();
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
