package com.example.advanced;

import android.app.Activity;
import android.os.Bundle;

import com.example.myapp.R;

public class ContentFraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ContentFragment frag=(ContentFragment)getFragmentManager().findFragmentById(R.id.content);
        Bundle extra=getIntent().getExtras();
        int position=extra.getInt("position");
        frag.setImage(position);
    }

}
