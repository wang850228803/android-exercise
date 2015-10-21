package com.example.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class UpdateService extends Service {
    
    private int status;
    public static final int INIT_STATUS = 0;
    public static final int CHECKING_STATUS = 1;
    public static final int CHECK_FINISH_NO_URL = 2;
    public static final int DOWNLOADING_STATUS = 3;
    public static final int DOWNLOAD_FINISH_STATUS = 4;
    public static final int CHECK_FINISH_WITH_URL = 5;
    
    private static final int MSG_CHECKING = 6;
    private static final int MSG_DOWNLOADING = 7;
    
    private int indexCount = 0;
    private static final String douban_url = "https://api.douban.com/v2/book/1220562";
    private String image_url = null;
    
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

        @Override
        public void startDownload() throws RemoteException {
            // TODO Auto-generated method stub
            mWorkHandler.sendEmptyMessage(MSG_DOWNLOADING);
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
                        startChecking();
                        break;
                    case MSG_DOWNLOADING:
                        setStatus(DOWNLOADING_STATUS);
                        downloadImage();
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
    
    private void startChecking() {
        HttpResponse httpResponse = null;
        HttpEntity entity;
        JSONObject info = null;
        
        HttpGet request = new HttpGet(douban_url);
        //有的网站会先判别用户的请求是否是来自浏览器，如不是，则返回不正确的文本，所以用httpclient抓取信息时在头部加入如下信息：
        request.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
        try {
            httpResponse = new DefaultHttpClient().execute(request);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (httpResponse != null) {
            entity = httpResponse.getEntity();
            try {
                info =  new JSONObject(EntityUtils.toString(entity));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (info != null) {
            try {
                image_url = info.getString("image");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (image_url == null) {
                setStatus(CHECK_FINISH_NO_URL);
            } else {
                setStatus(CHECK_FINISH_WITH_URL);
            }
        }
    }
    
    private void downloadImage() {
        int bytesum = 0;
        int byteread = 0;

        try {
            URL url = new URL(image_url);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            File file = new File("/data/data", "abc.gif");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream("/data/data/abc.gif");

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
