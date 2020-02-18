package com.example.demo10_22tcp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_conn, bt_send;
    private EditText et_ip, et_port, et_msg;
    private TextView tv_data;

    TCPClient tcpClient;
    Message message;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_data.append("接收消息：" + msg.obj.toString() + "\n");
                    break;
                case 2:
                    tv_data.append("发送消息：" + msg.obj.toString() + "\n");
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "TCP连接成功！", Toast.LENGTH_SHORT).show();
                    bt_send.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Clickbt();
        tcpClient = new TCPClient(handler);
    }

    private void Clickbt() {
        bt_conn.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    private void initView() {
        bt_conn = findViewById(R.id.bt_conn);
        bt_send = findViewById(R.id.bt_send);
        bt_send.setEnabled(false);
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);
        et_msg = findViewById(R.id.et_msg);
        tv_data = findViewById(R.id.tv_data);
        tv_data.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_conn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tcpClient.conntcp(et_ip.getText().toString(), Integer.parseInt(et_port.getText().toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.bt_send:
                tcpClient.send(et_msg.getText().toString());
                break;
        }
    }

}
