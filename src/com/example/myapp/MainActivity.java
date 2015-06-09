
package com.example.myapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.advanced.AidlTest;
import com.example.advanced.FragmentTest;
import com.example.advanced.MutilTouchDemoActivity;
import com.example.advanced.TestActivity;
import com.example.myapp.PlayService.LocalBinder;

public class MainActivity extends Activity implements View.OnClickListener {

    private Intent intent;
    PlayService mService;
    boolean mBound = false;
    Button playBtn;
    Button handlerBtn;
    Button threadHandlerBtn;

    private static final int SEND_MESSAGE_TO_UITHREAD = 0;
    private static final int WAIT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button calBtn = (Button) findViewById(R.id.cal);
        calBtn.setOnClickListener(this);
        playBtn = (Button) findViewById(R.id.play);
        playBtn.setOnClickListener(this);
        Button aidlBtn = (Button) findViewById(R.id.aidl);
        aidlBtn.setOnClickListener(this);
        Button receiverBtn = (Button) findViewById(R.id.receiver);
        receiverBtn.setOnClickListener(this);
        Button fraBtn = (Button) findViewById(R.id.frag);
        fraBtn.setOnClickListener(this);
        Button scrollBtn = (Button) findViewById(R.id.scrollview);
        scrollBtn.setOnClickListener(this);
        Button mtBtn = (Button) findViewById(R.id.multitouch);
        mtBtn.setOnClickListener(this);
        Button alBtn = (Button) findViewById(R.id.applist);
        alBtn.setOnClickListener(this);
        Button loaderBtn = (Button) findViewById(R.id.loader);
        loaderBtn.setOnClickListener(this);
        handlerBtn = (Button) findViewById(R.id.handler);
        handlerBtn.setOnClickListener(this);
        threadHandlerBtn = (Button) findViewById(R.id.threadHandler);
        threadHandlerBtn.setOnClickListener(this);
        Button handlerthreadBtn = (Button) findViewById(R.id.handlerthread);
        handlerthreadBtn.setOnClickListener(this);
        intent = new Intent();
        LocationManager locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 
        //判断GPS模块是否开启，如果没有则开启 
        if(!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) 
            Toast.makeText(this, "GPS is not open,Please open it!", Toast.LENGTH_SHORT).show(); 
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.cal:
                intent.setClass(MainActivity.this, Calculate.class);
                startActivity(intent);
                break;
            // localbinder service
            case R.id.play:
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
            case R.id.aidl:
                intent.setClass(MainActivity.this, AidlTest.class);
                startActivity(intent);
                break;
            case R.id.receiver:
                intent.setAction("com.example.action.MY_RECEIVER");
                sendBroadcast(intent);
                break;
            case R.id.frag:
                intent.setClass(MainActivity.this, FragmentTest.class);
                startActivity(intent);
                break;
            case R.id.scrollview:
                intent.setClass(MainActivity.this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.multitouch:
                intent.setClass(MainActivity.this, MutilTouchDemoActivity.class);
                startActivity(intent);
                break;
            // loader sample
            case R.id.applist:
                intent.setClass(MainActivity.this, ContactSampleListActivity.class);
                startActivity(intent);
                break;
            // provider loader
            case R.id.loader:
                intent.setClass(MainActivity.this, LoaderThrottle.class);
                startActivity(intent);
                break;
            case R.id.handler:
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
            case R.id.threadHandler:
                myThread.start();
                break;
            case R.id.handlerthread:
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
            default:
                break;
        }

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

}
