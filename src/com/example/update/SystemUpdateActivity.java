package com.example.update;

import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapp.R;

public class SystemUpdateActivity extends Activity {
    private TextView textView;
    private ProgressBar pBar;
    
    private ISystemUpdate mService;
    private WifiManager mWifiManager = null;
    private Context mContext;

    private int status;
    
    private int listenerIndex;

    private final class AppDeathRecipient implements IBinder.DeathRecipient {
        IBinder mBinder = null;

        public AppDeathRecipient (IBinder mBinder) {
            this.mBinder = mBinder;
        }
        
        @Override
        public void binderDied() {
            // TODO Auto-generated method stub
            mBinder.unlinkToDeath(AppDeathRecipient.this, 0);
        }
        
    }
    
    public class SystemUpdateListener extends ISystemUpdateListener.Stub {
        
        @Override
        public void updateUI(int status) throws RemoteException {
            // TODO Auto-generated method stub
            updateStatusUI();
        }
        
        @Override
        public void updateProgressBar() throws RemoteException {
            // TODO Auto-generated method stub
            
        }
    };

    ServiceConnection conn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            try {
                if(mService != null && listenerIndex != 0)
                    mService.removeListener(listenerIndex);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mService = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mService = ISystemUpdate.Stub.asInterface(service);
            AppDeathRecipient dRecipient = new AppDeathRecipient(mService.asBinder());
            try {
                mService.asBinder().linkToDeath(dRecipient, 0);
                listenerIndex = mService.addListener(new SystemUpdateListener());
                if (mWifiManager.isWifiEnabled())
                    startService(new Intent(mContext, UpdateService.class));
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.system_update);
        textView = (TextView)findViewById(R.id.text);
        pBar = (ProgressBar)findViewById(R.id.progress);
        status = UpdateService.INIT_STATUS;
        mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        bindService(new Intent(this, UpdateService.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
    }
    
    private void updateStatusUI() {
        textView.setText(R.string.check_update);
        textView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.VISIBLE);
    }
}
