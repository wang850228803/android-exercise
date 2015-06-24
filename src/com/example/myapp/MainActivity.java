
package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advanced.AidlTest;
import com.example.advanced.FragmentTest;
import com.example.advanced.MutilTouchDemoActivity;
import com.example.advanced.TestActivity;
import com.example.myapp.PlayService.LocalBinder;

public class MainActivity extends ListActivity{

    private Intent intent;
    PlayService mService;
    boolean mBound = false;
    Button playBtn;
    Button handlerBtn;
    Button threadHandlerBtn;
    Context mContext;

    private static final int SEND_MESSAGE_TO_UITHREAD = 0;
    private static final int WAIT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        MyAdapter adapter = new MyAdapter();
        
        /*List<String> items = new ArrayList<String>();
        items.add("test");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);*/
        
        setListAdapter(adapter);
        
        intent = new Intent();
        LocationManager locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 
        //判断GPS模块是否开启，如果没有则开启 
        if(!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) 
            Toast.makeText(this, "GPS is not open,Please open it!", Toast.LENGTH_SHORT).show(); 
        
        MyReceiver mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.action.MY_RECEIVER");
        mContext.registerReceiver(mReceiver,intentFilter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, PlayService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get
            // LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private class TestHandler extends Handler {

        private static final int COUNT = 0;
        private static final int STOP_COUNT = 1;
        int count = 0;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNT:
                    if (count < 10) {
                        Log.i("myapp", "in the message");
                        count++;
                        Toast.makeText(MainActivity.this, "count：" + count, Toast.LENGTH_SHORT)
                                .show();
                        sendEmptyMessageDelayed(TestHandler.COUNT, 4000);
                    } else {
                        sendEmptyMessage(TestHandler.STOP_COUNT);

                    }
                    break;
                default:
                    break;

            }
        }

    }

    private Handler uiHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_MESSAGE_TO_UITHREAD:
                    threadHandlerBtn.setTextColor(0);
            }
        }

    };

    private Thread myThread = new Thread() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            for (int i = 0; i < 3; i++) {
                try {
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(2000);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            uiHandler.obtainMessage(SEND_MESSAGE_TO_UITHREAD).sendToTarget();
        }

    };

    public class MyAdapter extends BaseAdapter{
        String[] array = {"calculate", "play music", "aidl test", "test receiver", "fragment test", "scrollview", 
                "multitouch", "applist", "loader", "handler", "threadHandler", "handlerthread"}; 

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            //这个不能不写
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        
        class ViewHolder {
            TextView text;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view;
            ViewHolder mViewHolder;
            LayoutInflater mInfater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                view = mInfater.inflate(android.R.layout.simple_list_item_1, null);//第二个参数为null
                mViewHolder.text = (TextView)view.findViewById(android.R.id.text1);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder)view.getTag();
            }
            mViewHolder.text.setText(array[position]);
            return view;
        }
        
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
            // TODO Auto-generated method stub
            switch (position) {
                case 0:
                    intent.setClass(MainActivity.this, Calculate.class);
                    startActivity(intent);
                    break;
                // localbinder service
                case 1:
                    if (playBtn.getText().equals("play music")) {
                        playBtn.setText("stop music");
                        onStart();
                        mService.play();
                    } else {
                        playBtn.setText("play music");
                        mService.stopPlay();
                        onStop();
                    }
                    break;
                case 2:
                    intent.setClass(MainActivity.this, AidlTest.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent.setAction("com.example.action.MY_RECEIVER");
                    sendBroadcast(intent,"test.permission");
                    //sendBroadcast(intent,android.Manifest.permission.WRITE_SECURE_SETTINGS);
                    //sendBroadcast(intent);
                    break;
                case 4:
                    intent.setClass(MainActivity.this, FragmentTest.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent.setClass(MainActivity.this, TestActivity.class);
                    startActivity(intent);
                    break;
                case 6:
                    intent.setClass(MainActivity.this, MutilTouchDemoActivity.class);
                    startActivity(intent);
                    break;
                // loader sample
                case 7:
                    intent.setClass(MainActivity.this, ContactSampleListActivity.class);
                    startActivity(intent);
                    break;
                // provider loader
                case 8:
                    intent.setClass(MainActivity.this, LoaderThrottle.class);
                    startActivity(intent);
                    break;
                case 9:
                    TestHandler mHandler = new TestHandler();
                    Log.i("myapp", "BeforeSendMessage");
                    mHandler.sendEmptyMessage(TestHandler.COUNT);
                    Log.i("myapp", "AfterSendMessage");
                    try {
                        synchronized (Thread.currentThread()) {
                            Thread.currentThread().wait(3000);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    myThread.start();
                    break;
                case 11:
                    HandlerThread ht = new HandlerThread("test");
                    ht.start();
                    // handler是在它关联的looper线程中处理消息的
                    Handler handler = new Handler(ht.getLooper()) {

                        @Override
                        public void handleMessage(Message msg) {
                            // TODO Auto-generated method stub
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case WAIT:
                                    Toast.makeText(
                                            MainActivity.this,
                                            "The current thread name:"
                                                    + Thread.currentThread().getName(),
                                            Toast.LENGTH_SHORT).show();
                                    System.out
                                            .println("Handler--->" + Thread.currentThread().getName());
                                    break;
                            }

                        }

                    };
                    handler.obtainMessage(WAIT).sendToTarget();
                    break;
                default:
                    break;
            }

        }
        
}
