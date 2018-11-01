package com.lixxy.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    //获取service发送的消息
                    Log.e(TAG, msg.getData().getString("service"));

                    break;
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.mBtnBind);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于服务端和客服端是两个APP,所以不能通过
                // Intent intent = new //Intent(this,MessengerService.class)获取Intent
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.lixxy.service", "com.lixxy.service.MessengerService"));
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(service);

            Bundle bundle = new Bundle();
            bundle.putString("client", "Hello,Services");

            Message message = Message.obtain();
            message.what = 100;
            message.setData(bundle);
            //如果客户端同时要接受服务器发的消息,需要加这句
            message.replyTo = mMessenger;
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "连接失败");
        }
    };

}
