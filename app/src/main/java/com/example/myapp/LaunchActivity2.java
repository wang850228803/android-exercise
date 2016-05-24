package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LaunchActivity2 extends Activity {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch2);
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText(this.toString() + "  " + this.getTaskId());
        Button but = (Button) findViewById(R.id.button2);
        ctx = this;
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, LaunchActivity.class));
            }
        });
    }
}
