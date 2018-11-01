package com.lixxy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "连接了");
        return mMessenger.getBinder();
    }


    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    //获取client发送的消息
                    Log.e(TAG, msg.getData().getString("client"));

                    //收到消息后给client发送消息
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("service", "Hello,Client");
                    message.setData(bundle);
                    message.what = 200;
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
    }

}
