package com.example.advanced;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.R;

public class ContentFragment extends Fragment {
    
    private View mContentView;
    private ImageView mImage;
    private Bitmap mBitmap=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContentView=inflater.inflate(R.layout.content_fragment, null);
        mImage=(ImageView)mContentView.findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "-------------test---------", Toast.LENGTH_SHORT).show();
            }
        });
        return mContentView;
    }

    public void setImage(int position){
        switch(position){
            case 0:
                mBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.girl);
                mImage.setImageBitmap(mBitmap);
                break;
            case 1:
                mBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.shortcut);
                mImage.setImageBitmap(mBitmap);
                break;
            case 2:
                mBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.animal);
                mImage.setImageBitmap(mBitmap);
                break;
            default:
                break;
        }
        
    }
}
