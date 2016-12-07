package com.example.advanced;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapp.MainActivity;
import com.example.myapp.R;

public class FragmentLifecycle extends Activity implements View.OnClickListener{
    FragmentTransaction ft;
    FragmentManager fm;
    Button bt1;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_lifecycle);
        bt1 = (Button)findViewById(R.id.attach1);
        bt1.setOnClickListener(this);
        bt2 = (Button)findViewById(R.id.detach2);
        bt2.setOnClickListener(this);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.frag, new MainActivity.MyFragment(), "frag1");
        ft.add(R.id.frag, new ContentFragment(), "frag2");
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attach1:
                fm.beginTransaction().attach(fm.findFragmentByTag("frag1")).commitAllowingStateLoss();
                break;
            case R.id.detach2:
                fm.beginTransaction().detach(fm.findFragmentByTag("frag2")).commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
