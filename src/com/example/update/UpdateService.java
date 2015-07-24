package com.example.update;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.os.DropBoxManager.Entry;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class UpdateService extends Service {
    
    private int indexCount = 0;
    
    private int status;
    public static int INIT_STATUS = 0;
    public static int CHECKING_STATUS = 1;
    public static int CHECK_FINISH_STATUS = 2;
    public static int DOWNLOADING_STATUS = 3;
    public static int DOWNLOAD_FINISH_STATUS = 4;
    
    private static final int MSG_CHECKING = 6;
    
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    
    private ConcurrentHashMap<Integer, ISystemUpdateListener> listenerMap = new ConcurrentHashMap<Integer, ISystemUpdateListener>();
    private ConcurrentHashMap<Integer, AppDeathRecipient> deatHashMap = new ConcurrentHashMap<Integer, UpdateService.AppDeathRecipient>();

    private final class AppDeathRecipient implements IBinder.DeathRecipient {
        IBinder mBinder = null;
        int index;

        public AppDeathRecipient (IBinder mBinder, int index) {
            this.mBinder = mBinder;
            this.index = index;
        }
        
        @Override
        public void binderDied() {
            // TODO Auto-generated method stub
            listenerMap.remove(index);
            deatHashMap.remove(index);
            mBinder.unlinkToDeath(AppDeathRecipient.this, 0);
        }
        
    }
    
    public final ISystemUpdate.Stub binder = new ISystemUpdate.Stub() {
        
        @Override
        public void removeListener(int index) throws RemoteException {
            // TODO Auto-generated method stub
            listenerMap.remove(index);
            deatHashMap.remove(index);
        }
        
        @Override
        public int addListener(ISystemUpdateListener listener) throws RemoteException {
            // TODO Auto-generated method stub
            if (listener != null) {
                listenerMap.put(++indexCount, listener);
                AppDeathRecipient recipient = new AppDeathRecipient(listener.asBinder(), indexCount);
                listener.asBinder().linkToDeath(recipient, 0);
                deatHashMap.put(indexCount, recipient);
                return indexCount;
            }
            return 0;
        }
    };
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        status = INIT_STATUS;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        startWorkThread();
        mWorkHandler.sendEmptyMessage(MSG_CHECKING);
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void startWorkThread() {
        mWorkThread = new HandlerThread("WorkThread");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_CHECKING:
                        setStatus(CHECKING_STATUS);
                        break;

                    default:
                        break;
                }
            }
            
        };
    }
    
    private void setStatus(int status) {
        this.status = status;
        Iterator iterator = listenerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ISystemUpdateListener listener = (ISystemUpdateListener)entry.getValue();
            try {
                listener.updateUI(status);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
