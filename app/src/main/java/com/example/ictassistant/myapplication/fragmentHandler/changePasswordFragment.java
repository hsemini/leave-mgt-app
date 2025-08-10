package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ictassistant.myapplication.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.ictassistant.myapplication.LoginActivity.CONNECTION_TIMEOUT;
import static com.example.ictassistant.myapplication.LoginActivity.READ_TIMEOUT;

/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.13
 * Office       :IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Handle change password fragment details..
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.13      Ms.K.A.H.Semini             Created
 * (02)    2017.12.01      Ms.K.A.H.Semini             Connect with server db
 * (03)
 * -------------------------------------------------------------------------------------------------
 */

public class changePasswordFragment extends Fragment {

    TextView tv_old,tv_new,tv_confirm;
    EditText et_old,et_new,et_confirm;
    String emp_no,old_password,new_password,confirm_password;
    Button bt_ok,bt_cancel;

    String checkEmpty,matchData,fetchPassword;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emp_no = activity.getIntent().getStringExtra("emp_no");
        //Toast.makeText(getActivity(), emp_no, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View changePasswordFragment = inflater.inflate(R.layout.fragment_change_password, container, false);

        UIInitializer(changePasswordFragment);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                new changePasswordFragment.AsyncGetPassword().execute(emp_no);

//                checkEmpty = checkEmpty();
//
//                if(checkEmpty.equals("true")){
//                    matchData = matchData();
//                    if(matchData.equals("true")){
//                        new changePasswordFragment.AsyncUpdatePasswordDetails().execute(emp_no,new_password);
//                    }
//                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        return changePasswordFragment;
    }

    void UIInitializer(View v){
        tv_old = v.findViewById(R.id.tv_old_password);
        tv_new = v.findViewById(R.id.tv_new_password);
        tv_confirm = v.findViewById(R.id.tv_confirm_password);
        et_old = v.findViewById(R.id.et_old_password);
        et_new = v.findViewById(R.id.et_new_password);
        et_confirm = v.findViewById(R.id.et_confirm_password);
        bt_ok = v.findViewById(R.id.bt_password_ok);
        bt_cancel = v.findViewById(R.id.bt_password_cancel);
    }

    void getData(){
        old_password=et_old.getText().toString();
        new_password=et_new.getText().toString();
        confirm_password= et_confirm.getText().toString();
    }

    void clearData(){
        et_old.getText().clear();
        et_new.getText().clear();
        et_confirm.getText().clear();
    }

    String matchData(){

        String match ="true";
//        new changePasswordFragment.AsyncGetPassword().execute(emp_no);
//        Toast.makeText(getActivity(), fetchPassword, Toast.LENGTH_LONG).show();
        if(!new_password.equals(confirm_password) || !old_password.equals(fetchPassword) ){
            match = "false";

            if(!new_password.equals(confirm_password) && !old_password.equals(fetchPassword)) {
                Toast.makeText(getActivity(), "Input your data more carefully... ", Toast.LENGTH_LONG).show();
                tv_new.setTextColor(Color.RED);
                tv_confirm.setTextColor(Color.RED);
                tv_old.setTextColor(Color.RED);
                et_old.getText().clear();
                et_new.getText().clear();
                et_confirm.getText().clear();
            }else if(!new_password.equals(confirm_password)){
                Toast.makeText(getActivity(), "New password does not match which with confirm password..", Toast.LENGTH_LONG).show();
                tv_new.setTextColor(Color.RED);
                tv_confirm.setTextColor(Color.RED);
                et_new.getText().clear();
                tv_old.setTextColor(R.color.textColor);
                et_confirm.getText().clear();
            }else if(!old_password.equals(fetchPassword)){
                Toast.makeText(getActivity(), "Previous password is incorrect...", Toast.LENGTH_LONG).show();
                tv_old.setTextColor(Color.RED);
                et_old.getText().clear();
                tv_new.setTextColor(R.color.textColor);
                tv_confirm.setTextColor(R.color.textColor);
            }
        }

        return match;
    }

    String checkEmpty(){
        String empty="true";
        if(old_password.trim().length()<1 || new_password.trim().length()<1 || confirm_password.trim().length()<1){
            empty="false";
            Toast.makeText(getActivity(), "Please fill all fields...", Toast.LENGTH_LONG).show();

            if(old_password.trim().length()<1 && new_password.trim().length()<1 && confirm_password.trim().length()<1){
                tv_old.setTextColor(Color.RED);
                tv_new.setTextColor(Color.RED);
                tv_confirm.setTextColor(Color.RED);
            }else if(old_password.trim().length()<1){
                tv_old.setTextColor(Color.RED);
                tv_new.setTextColor(R.color.textColor);
                tv_confirm.setTextColor(R.color.textColor);
            }else if(new_password.trim().length()<1){
                tv_new.setTextColor(Color.RED);
                tv_old.setTextColor(R.color.textColor);
                tv_confirm.setTextColor(R.color.textColor);
            }else if(confirm_password.trim().length()<1){
                tv_confirm.setTextColor(Color.RED);
                tv_new.setTextColor(R.color.textColor);
                tv_old.setTextColor(R.color.textColor);
            }

        }
        return empty;
    }

    private class AsyncUpdatePasswordDetails extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            //     pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://100.64.183.49/webService/updatePassword.inc.php");//TODO change IP address

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("emp_no", params[0])
                        .appendQueryParameter("password", params[1]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();
            //Toast.makeText(getActivity(), "Yes....inside", Toast.LENGTH_LONG).show();
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(getActivity(), "You have changed your password successfully!!", Toast.LENGTH_LONG).show();
            }else if (result.equalsIgnoreCase("false")){
                Toast.makeText(getActivity(), "Failed to change your password.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Failed to change your password.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }

            clearData();

        }

    }

    private class AsyncGetPassword extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
//            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://100.64.183.49/webService/fetchPassword.inc.php");//TODO change IP address

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("emp_no", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            fetchPassword=result;
            checkEmpty = checkEmpty();

            if(checkEmpty.equals("true")){
                matchData = matchData();
                if(matchData.equals("true")){
                    new changePasswordFragment.AsyncUpdatePasswordDetails().execute(emp_no,new_password);
                }
            }
        }

    }

}
