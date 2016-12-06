
package com.example.myapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.advanced.AidlTest;
import com.example.advanced.ContentFragment;
import com.example.advanced.FragmentTest;
import com.example.advanced.MutilTouchDemoActivity;
import com.example.advanced.TestActivity;
import com.example.myapp.PlayService.LocalBinder;
import com.example.update.SystemUpdateActivity;

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
    
    Handler myHandler = null;

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
        
        /*MyReceiver mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.action.MY_RECEIVER");
        mContext.registerReceiver(mReceiver,intentFilter);*/
        Button test = (Button)findViewById(R.id.test);
        test.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
                return false;
            }

        });
        test.setClickable(false);
        test.setText(getClass().getName());
        //test.setEnabled(false);

        ContentFragment fragment = new ContentFragment();
        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).addToBackStack(null).commit();
        getFragmentManager().executePendingTransactions();

        /*try {
            getFragmentManager().popBackStack();
            getFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            Log.i(this + "", "enter catch");
            getFragmentManager().beginTransaction().remove(fragment).commit();
            getFragmentManager().executePendingTransactions();
        }

        Log.i(this + "", getFragmentManager().findFragmentById(android.R.id.content)+"");*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment1 = getFragmentManager().findFragmentById(android.R.id.content);
        LinearLayout layout = (LinearLayout) fragment1.getView();
        layout.getChildAt(0).performClick();
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
        String[] array = {"calculate", "play music", "aidl test", "test receiver", "fragment test",
                "scrollview", "multitouch", "applist", "loader", "handler",
                "threadHandler", "handlerthread", "disableConnect", "OTA", "postdelayed",
                "stop postdelayed","popup","launch_mode","fragment_lifecycle"};

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
                if (position == 1 || position == 2 || position == 3) {
                    view = mInfater.inflate(android.R.layout.simple_list_item_1, null);//第二个参数为null
                    mViewHolder.text = (TextView) view.findViewById(android.R.id.text1);
                }
                else {
                    view = mInfater.inflate(R.layout.color_item, null);
                    mViewHolder.text = (TextView)view.findViewById(R.id.text1);
                }
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (ViewHolder)view.getTag();
            }
            mViewHolder.text.setText(array[position]);
           // Toast.makeText(MainActivity.this, "show view at:" + position, Toast.LENGTH_SHORT).show();
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
                    //sendBroadcast(intent,"test.permission");
                    //sendBroadcast(intent,android.Manifest.permission.WRITE_SECURE_SETTINGS);
                    sendBroadcast(intent);
                    //sendBroadcastAsUser(intent, UserHandle.ALL);
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
                    Log.d("test", handler.hasMessages(WAIT) + "");
                    break;
                case 12:
                    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                    wifiManager.disconnect();
                    //wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
                    int test = Settings.Global.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON,1);
                    Log.i("test", test+"");
                    int loc = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 0);
                    Toast.makeText(mContext, "location="+loc, Toast.LENGTH_LONG).show();
                    break;
                case 13:
                    startActivity(new Intent(MainActivity.this, SystemUpdateActivity.class));
                    break;
                case 14:
                    myHandler = new Handler();
                    myHandler.postDelayed(runnable, 5000);
                    break;
                case 15:
                    myHandler.removeCallbacks(runnable);
                    break;
                case 16:
                    //Log.d("POPUP", ((ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0)).getChildAt(0) + "");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    MyFragment fragment = new MyFragment();
                    transaction.add(android.R.id.content, fragment,"tag").addToBackStack(null).commit();
                    getFragmentManager().executePendingTransactions();
                    Log.d("POPUP", getFragmentManager().findFragmentById(android.R.id.content) + "");
                    Log.d("POPUP", getFragmentManager().findFragmentByTag("tag") + "");

                    break;
                case 17:
                    startActivity(new Intent(this, LaunchActivity.class));
                    break;
                case 18:
                    break;
                default:
                    break;
            }

        }


    private Runnable runnable = new Runnable() {
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Toast.makeText(mContext, "postdelay", Toast.LENGTH_LONG).show();
        }
    };

    public static class MyFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View mContentView=inflater.inflate(R.layout.content_fragment, null);
            ImageView mImage=(ImageView)mContentView.findViewById(R.id.image);
            mImage.setImageResource(R.drawable.gallery_photo_1);
            return mContentView;
        }
    }
}
