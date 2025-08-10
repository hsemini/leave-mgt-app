package com.example.ictassistant.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ictassistant.myapplication.fragmentHandler.aboutFragment;
import com.example.ictassistant.myapplication.fragmentHandler.changePasswordFragment;
import com.example.ictassistant.myapplication.fragmentHandler.homeFragment;
import com.example.ictassistant.myapplication.fragmentHandler.leaveApplyFragment;
import com.example.ictassistant.myapplication.fragmentHandler.leaveHistoryFragment;
import com.example.ictassistant.myapplication.fragmentHandler.profileFragment;
import com.example.ictassistant.myapplication.fragmentHandler.users;

/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.10
 * Office       : IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Handle home page.
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.10      Ms.K.A.H.Semini             Created
 * (02)    2017.12.09      Ms.K.A.H.Semini             Finalize the code in version 1.0
 * (03)
 * -------------------------------------------------------------------------------------------------
 */

public class HomePage extends AppCompatActivity {

    String emp_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Application");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        emp_no= getIntent().getExtras().getString("emp_no");
        setHomeFragment();  //set home fragment in to home page(default page)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //set menu bar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_home                       :   setHomeFragment();break;
            case R.id.action_Log_Off                    :   setLogOff();break;
            case R.id.action_profile                    :   setProfileFragment();break;
            case R.id.action_users                      :   setUsersFragment();break;
            case R.id.action_apply_leave                :   setRequestLeaveFragment();break;
            case R.id.action_leave_history              :   setLeaveHistoryFragment();break;
            case R.id.action_change_password            :   setChangePasswordFragment();break;
            case R.id.action_about                      :   setAboutFragment();break;
            default                                     :   return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //to load home page
    public void setHomeFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        homeFragment homeFragment = new homeFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, homeFragment,"homeFragment");
        FT.commit();
    }

    //to load profile
    public void setProfileFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        profileFragment profileFragment = new profileFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, profileFragment,"profileFragment");
        FT.commit();
    }


    public void setUsersFragment(){
      //Alow to access only the chief secretary //TODO add chief secretary sir and perera sir emp no
      if(emp_no.equals("1001")) {
          android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
          android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

          users users = new users();
          removePreviousFragments(FT);
          FT.add(R.id.fragmentHandler, users, "users");
          FT.commit();
      }else{
          Toast.makeText(HomePage.this, "This Feature can be accessed staff officers only!", Toast.LENGTH_LONG).show();
      }
    }

    //to load leave request page
    public void setRequestLeaveFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        leaveApplyFragment leaveApplyFragment = new leaveApplyFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, leaveApplyFragment,"requestLeaveFragment");
        FT.commit();
    }

    //to load leave history page
    public void setLeaveHistoryFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        leaveHistoryFragment leaveHistoryFragment = new leaveHistoryFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, leaveHistoryFragment,"leaveHistoryFragment");
        FT.commit();
    }

    //to load change password page
    public void setChangePasswordFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        changePasswordFragment changePasswordFragment = new changePasswordFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, changePasswordFragment,"changePasswordFragment");
        FT.commit();
    }

    // to load about page
    public void setAboutFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();

        aboutFragment aboutFragment = new aboutFragment();
        removePreviousFragments(FT);
        FT.add(R.id.fragmentHandler, aboutFragment,"aboutFragment");
        FT.commit();
    }

    //set log off action
    public void setLogOff(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();
        removePreviousFragments(FT);
        FT.commit();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //to remove the previous loading page
    public void removePreviousFragments(android.support.v4.app.FragmentTransaction FT) {

        Fragment aboutFragment = getSupportFragmentManager().findFragmentByTag("aboutFragment");
        if (aboutFragment != null) {
            FT.remove(aboutFragment);
        }

        Fragment changePasswordFragment = getSupportFragmentManager().findFragmentByTag("changePasswordFragment");
        if (changePasswordFragment != null) {
            FT.remove(changePasswordFragment);
        }

        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("homeFragment");
        if (homeFragment != null) {
            FT.remove(homeFragment);
        }

        Fragment leaveApplyFragment = getSupportFragmentManager().findFragmentByTag("requestLeaveFragment");
        if (leaveApplyFragment != null) {
            FT.remove(leaveApplyFragment);
        }

        Fragment leaveHistoryFragment = getSupportFragmentManager().findFragmentByTag("leaveHistoryFragment");
        if (leaveHistoryFragment != null) {
            FT.remove(leaveHistoryFragment);
        }

        Fragment profileFragment = getSupportFragmentManager().findFragmentByTag("profileFragment");
        if (profileFragment != null) {
            FT.remove(profileFragment);
        }

        Fragment users = getSupportFragmentManager().findFragmentByTag("users");
        if (users != null) {
            FT.remove(users);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK      :  return true;
                case KeyEvent.KEYCODE_HOME      :  return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }


}
