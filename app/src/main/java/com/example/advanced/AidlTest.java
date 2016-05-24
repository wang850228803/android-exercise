package com.example.advanced;

import com.example.myapp.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class AidlTest extends Activity implements OnClickListener{

    IDelay mBindService=null;
    ProgressBar progress;
    boolean isBind=false;
    private static final int PROGRESSBAR=0;
    
    private ServiceConnection connection=new ServiceConnection() {
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBindService=IDelay.Stub.asInterface(service);
            try {
                mBindService.registCallback(mCallback);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBindService=null;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_test_activity);
        progress=(ProgressBar)findViewById(R.id.progress);
        setProgressBarVisibility(false);
        Button start=(Button)findViewById(R.id.startService);
        start.setOnClickListener(this);
        Button plus=(Button)findViewById(R.id.plus);
        plus.setOnClickListener(this);
        Button min=(Button)findViewById(R.id.min);
        min.setOnClickListener(this);
        Button bind=(Button)findViewById(R.id.bind);
        bind.setOnClickListener(this);
        Button unbind=(Button)findViewById(R.id.unbind);
        unbind.setOnClickListener(this);
    }
    
    

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.startService:
                if (mBindService==null)
                    break;
                try {
                    mBindService.executeDelay();
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.plus:
                Message msg=new Message();
                Bundle b=new Bundle();
                b.putInt("data", 0);
                msg.what=PROGRESSBAR;
                msg.setData(b);
                mHandler.sendMessage(msg);
                break;
            case R.id.min:
                Message msg1=new Message();
                Bundle b1=new Bundle();
                b1.putInt("data", 1);
                msg1.what=PROGRESSBAR;
                msg1.setData(b1);
                mHandler.sendMessage(msg1);
                break;
            case R.id.bind:
                bindService(new Intent(this, Delay.class), connection, Context.BIND_AUTO_CREATE);
                isBind=true;
                break;
            case R.id.unbind:
                if(isBind){
                    if(mBindService!=null){
                        try {
                            mBindService.unRegistCallback(mCallback);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                unbindService(connection);
                isBind=false;
            default:
                break;
        }
        
    }



   /* @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Intent intent0=new Intent(this,Delay.class);
        this.bindService(intent0, connection, Context.BIND_AUTO_CREATE);
    }*/

   /* @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        this.unbindService(connection);
    }*/

    
   final Handler mHandler=new Handler(){

    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        int s=msg.getData().getInt("data");
        switch (s) {
            case 0:
                progress.incrementProgressBy(10);
                break;
            case 1:
                progress.incrementProgressBy(-10);
            default:
                break;
        }
        
    }
       
   };
   
   private IRemoteCallback mCallback=new IRemoteCallback.Stub() {
    
    @Override
    public void valueChanged(int value) throws RemoteException {
        // TODO Auto-generated method stub
        Message msg=new Message();
        Bundle b=new Bundle();
        b.putInt("date", 1);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }
};
}
