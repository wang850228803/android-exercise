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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapp.R;

public class SystemUpdateActivity extends Activity {
    
    private static final int UPDATE_STATUS = 10;
    private static final int UPDATE_PROGRESS = 11;
    private TextView textView;
    private ProgressBar pBar;
    private ProgressBar dBar;
    private Button cancleBtn;
    private Button okBtn;
    
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
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_STATUS:
                    updateStatusUI();
                    break;

                default:
                    break;
            }
        }
        
    };
    
    public class SystemUpdateListener extends ISystemUpdateListener.Stub {
        
        @Override
        public void updateUI(int status) throws RemoteException {
            // TODO Auto-generated method stub
            SystemUpdateActivity.this.status = status;
            mHandler.sendEmptyMessage(UPDATE_STATUS);
        }
        
        @Override
        public void updateProgressBar(int progress) throws RemoteException {
            // TODO Auto-generated method stub
            dBar.setProgress(progress);
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
        textView = (TextView)findViewById(R.id.update_text);
        pBar = (ProgressBar)findViewById(R.id.update_progress);
        dBar = (ProgressBar)findViewById(R.id.download_progress);
        
        cancleBtn = (Button)findViewById(R.id.cancel);
        cancleBtn.setOnClickListener(listener);
        okBtn = (Button)findViewById(R.id.ok);
        okBtn.setOnClickListener(listener);
        status = UpdateService.INIT_STATUS;
        mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        bindService(new Intent(this, UpdateService.class), conn, Context.BIND_AUTO_CREATE);
    }
    
    OnClickListener listener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.cancel:
                    if (status == UpdateService.CHECKING_STATUS)
                        SystemUpdateActivity.this.finish();
                    else if (status == UpdateService.DOWNLOADING_STATUS)
                        //stop download...
                    break;
                case R.id.ok:
                    if (status == UpdateService.CHECK_FINISH_NO_URL)
                        SystemUpdateActivity.this.finish();
                    else if (status == UpdateService.CHECK_FINISH_WITH_URL)
                        try {
                            mService.startDownload();
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    else if (status == UpdateService.DOWNLOAD_FINISH_STATUS) {
                        //do something
                    }
                    break;
                    
                default:
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
    }
    
    private void updateStatusUI() {
        switch (status) {
            case UpdateService.CHECKING_STATUS:
                textView.setText(R.string.check_update);
                textView.setVisibility(View.VISIBLE);
                pBar.setVisibility(View.VISIBLE);
                cancleBtn.setVisibility(View.VISIBLE);
                break;
                
            case UpdateService.CHECK_FINISH_NO_URL:
                textView.setText(R.string.no_url);
                pBar.setVisibility(View.GONE);
                cancleBtn.setVisibility(View.GONE);
                okBtn.setVisibility(View.VISIBLE);
                break;
             
            case UpdateService.CHECK_FINISH_WITH_URL:
                textView.setText(R.string.check_finish);
                pBar.setVisibility(View.GONE);
                cancleBtn.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.VISIBLE);
                break;
            case UpdateService.DOWNLOADING_STATUS:
                textView.setText(R.string.downloading);
                pBar.setVisibility(View.GONE);
                dBar.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.GONE);
                break;
            
            case UpdateService.DOWNLOAD_FINISH_STATUS:
                textView.setText(R.string.download_finish);
                okBtn.setVisibility(View.VISIBLE);
                cancleBtn.setVisibility(View.GONE);

            default:
                break;
        }

    }
}
