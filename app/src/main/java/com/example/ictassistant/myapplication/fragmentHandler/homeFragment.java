package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ictassistant.myapplication.R;

/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.13
 * Office       : IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Default home page.
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.13      Ms.K.A.H.Semini             Created
 * (02)    2017.12.09      Ms.K.A.H.Semini             Updated according to layout changes
 * (03)
 * -------------------------------------------------------------------------------------------------
 */

public class homeFragment extends Fragment {

    String emp_no;
    ImageView homeImg;
    int[] mResources = { //this is store photos for the home page slide show.
            R.mipmap.photo1,
            R.mipmap.photo2,
            R.mipmap.photo3,
            R.mipmap.photo4,
            R.mipmap.photo5
    };
    int i = 0; // for continue the photo slide show

    //Retrieve this data from login page
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emp_no = activity.getIntent().getStringExtra("emp_no");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);

        homeImg = homeFragment.findViewById(R.id.homeImg);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                homeImg.setImageResource(mResources[i]);
                i++;
                if(i>mResources.length-1)
                {
                    i=0;
                }
                handler.postDelayed(this, 3000);  //for interval...
            }
        };
        handler.postDelayed(runnable, 3000); //for initial delay..

        return homeFragment;
    }



}
