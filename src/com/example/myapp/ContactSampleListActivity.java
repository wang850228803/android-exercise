package com.example.myapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class ContactSampleListActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FragmentManager fm=getFragmentManager();
        fm.beginTransaction().add(R.id.fragment , new AppListFragment()).commit();
    }
}
