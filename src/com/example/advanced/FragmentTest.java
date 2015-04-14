
package com.example.advanced;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.advanced.MainFragment.onItemClickListener;
import com.example.myapp.R;

public class FragmentTest extends Activity implements onItemClickListener {

    private Boolean mDual=false;
    private ContentFragment contentFrag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        //contentFrag=(ContentFragment)getFragmentManager().findFragmentById(R.id.content_fragment);
        if(contentFrag!=null) mDual=true;
    }

    @Override
    public void onItemClick(int position) {
        // TODO Auto-generated method stub
        Log.i("fragmentTest", "position="+position);
        if(mDual){
            contentFrag.setImage(position);
        }
        else{
            Intent intent0=new Intent(this, ContentFraActivity.class);
            intent0.putExtra("position", position);
            startActivity(intent0);
        }
    }


}
