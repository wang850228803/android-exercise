
package com.example.myapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.advanced.AidlTest;
import com.example.myapp.PlayService.LocalBinder;

public class MainActivity extends Activity implements View.OnClickListener {

    private Intent intent;
    PlayService mService;
    boolean mBound = false;
    Button playBtn;
    
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
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.cal:
                intent.setClass(MainActivity.this, Calculate.class);
                startActivity(intent);
                break;
            case R.id.play:
                if (playBtn.getText().equals("play music")){
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
            case R.id.receiver:
                intent.setAction("com.example.action.MY_RECEIVER");
                sendBroadcast(intent);
                break;
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

    /** Called when a button is clicked (the button in the layout file attaches to
      * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
