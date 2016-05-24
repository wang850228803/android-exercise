package com.example.advanced;

import com.example.myapp.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * 
 * @author xinye
 *
 */
public class TestActivity extends Activity {
    private static String TAG;
    
    private LinearLayout mContainer = null;
    private MyScrollView mScrollView = null;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) {
                             mScrollView.setScaleX(2);
                         }
                     });
        mScrollView = (MyScrollView) findViewById(R.id.scrollView);
        mContainer = (LinearLayout) findViewById(R.id.container);
        
        LayoutParams params = new LayoutParams(getWinWidth(), 800);
        
        ImageView imageView1 = new ImageView(this);
        Log.i(TAG, (imageView1==null)+"");
        imageView1.setLayoutParams(params);
        imageView1.setImageResource(R.drawable.gallery_photo_5);
        imageView1.setScaleType(ScaleType.FIT_CENTER);
        mContainer.addView(imageView1);
        
        ImageView imageView2 = new ImageView(this);
        imageView2.setLayoutParams(params);
        imageView2.setImageResource(R.drawable.gallery_photo_1);
        imageView2.setScaleType(ScaleType.FIT_CENTER);
        mContainer.addView(imageView2);
        
        ImageView imageView3 = new ImageView(this);
        imageView3.setLayoutParams(params);
        imageView3.setImageResource(R.drawable.gallery_photo_2);
        imageView3.setScaleType(ScaleType.FIT_CENTER);
        mContainer.addView(imageView3);
        
        
        ImageView imageView4 = new ImageView(this);
        imageView4.setLayoutParams(params);
        imageView4.setImageResource(R.drawable.gallery_photo_3);
        imageView4.setScaleType(ScaleType.FIT_CENTER);
        mContainer.addView(imageView4);
        
        
        ImageView imageView5 = new ImageView(this);
        imageView5.setLayoutParams(params);
        imageView5.setImageResource(R.drawable.gallery_photo_4);
        imageView5.setScaleType(ScaleType.FIT_CENTER);
        mContainer.addView(imageView5);
        
        
        
    }
    
    @Override
    protected void onResume() {
//        ((MyScrollView)mContainer.getParent()).init();
        super.onResume();
    }
    
    private int getWinWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    private int getWinHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
