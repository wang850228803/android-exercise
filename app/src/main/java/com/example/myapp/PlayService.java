package com.example.myapp;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class PlayService extends Service {

    private MediaPlayer mPlayer;
    private final Binder mBinder=new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }
    
    public class LocalBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }
    }
    public void onCreate() {       
        super.onCreate();
        
    }
    
    public void play(){
        mPlayer=MediaPlayer.create(this, R.raw.gequ);
        mPlayer.start();
    }
    
    public void stopPlay(){
        mPlayer.stop();
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    public int getRandomNumber() {
        return new Random().nextInt(100);
      }
}
