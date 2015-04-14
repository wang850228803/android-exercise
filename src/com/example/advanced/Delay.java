package com.example.advanced;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class Delay extends Service {
    
    final RemoteCallbackList<IRemoteCallback> mCallbacks=new RemoteCallbackList<IRemoteCallback>();
    public final int VALUE_CHANGE=1;
    int value=0;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessage(VALUE_CHANGE);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        mCallbacks.kill();
        mHandler.removeMessages(VALUE_CHANGE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        
        return mBinder;
    }

    private final IDelay.Stub mBinder=new IDelay.Stub(){

        @Override
        public void executeDelay() throws RemoteException {
            // TODO Auto-generated method stub
                try {
                    Thread.sleep(2000);
                    Log.i("aidl", "delay 2 sec");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        @Override
        public void registCallback(IRemoteCallback mCallback) throws RemoteException {
            // TODO Auto-generated method stub
            mCallbacks.register(mCallback);
        }

        @Override
        public void unRegistCallback(IRemoteCallback mCallback) throws RemoteException {
            // TODO Auto-generated method stub
            mCallbacks.unregister(mCallback);
        }
        
    };
    
    private final Handler mHandler=new Handler(){
        
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what){
                case VALUE_CHANGE:
                    value++;
                    int N=mCallbacks.beginBroadcast();
                    for(int i=0;i<N;i++){
                        try {
                            mCallbacks.getBroadcastItem(i).valueChanged(value);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    mCallbacks.finishBroadcast();
                    sendMessageDelayed(obtainMessage(VALUE_CHANGE),1000);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
        
    };
}
