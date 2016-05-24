package com.example.advanced;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends ListFragment {
    
    public onItemClickListener mListener;
    
    public interface onItemClickListener{
        public void onItemClick(int position);
    }
    
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        try{
            mListener=(onItemClickListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + "must implement listener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        String[] items={"one","two", "three"};
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        mListener.onItemClick(position);
    }

}
