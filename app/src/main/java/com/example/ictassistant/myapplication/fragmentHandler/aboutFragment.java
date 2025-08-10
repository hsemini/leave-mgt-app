package com.example.ictassistant.myapplication.fragmentHandler;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ictassistant.myapplication.R;


/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.13
 * Office       :IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Handle about fragment details..
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.13      Ms.K.A.H.Semini             Created
 * (02)
 * -------------------------------------------------------------------------------------------------
 */

public class aboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View aboutFragment = inflater.inflate(R.layout.fragment_about, container, false);
        return aboutFragment;
    }

}