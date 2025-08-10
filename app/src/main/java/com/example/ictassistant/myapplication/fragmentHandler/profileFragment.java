package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Date         : 2017.10.09
 * Office       : IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Handle profile section of the main menu..
 * -----------------------------------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.09      Ms.K.A.H.Semini             Created
 * (02)    2017.11.20      Ms.K.A.H.Semini             Replace code as fetching data from the server db
 * (03)    2017.12.09      Ms.K.A.H.Semini             Finalize the code of version 1.0
 * (04)
 * ------------------------------------------------------------------------------------------------------------------------------
 */

public class profileFragment extends Fragment {

    TextView tvEmpNo, tvUserName;
    EditText etPhoneNo, etPost;
    String phoneNo,post,detailSet,update_phoneNo,update_post;
    Button btUpload,btCancle;
    ImageView ivEdit;
    String[] details;
    public String emp_no;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emp_no = activity.getIntent().getStringExtra("emp_no");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myProfile = inflater.inflate(R.layout.fragment_profile, container, false);
        UIcomponentInitilizer(myProfile);

        //Get profile details from server according to emp_no
        new AsyncGetProfileDetails().execute(emp_no);
        return myProfile;
    }

    //Get profile details from server according to emp_no
    private class AsyncGetProfileDetails extends AsyncTask<String, String, String>
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
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where the php file resides
                url = new URL("http://100.64.183.49/webService/fetchProfileDetails.inc.php");//TODO change IP address

            } catch (MalformedURLException e) {
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
            detailSet=result;
            details = detailSet.split(",");
            setData();

            ivEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    convertDataToString();
                    etPhoneNo.setEnabled(true);
                    etPost.setEnabled(true);
                    btUpload.setVisibility(View.VISIBLE);
                    btCancle.setVisibility(View.VISIBLE);

                    btUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setTitle("Confirm dialog box...");
                            builder.setMessage("Are you sure that you want to update your details?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    emp_no = tvEmpNo.getText().toString();
                                    update_phoneNo= etPhoneNo.getText().toString();
                                    update_post = etPost.getText().toString();

                                    if(update_phoneNo.length()==10) {
                                        new AsyncUpdateProfileDetails().execute(emp_no,update_post,update_phoneNo);
                                    }else{
                                        Toast.makeText(getActivity(), "Phone number should contain 10 numbers...", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    etPost.setText(post);
                                    etPhoneNo.setText(phoneNo);
                                    etPhoneNo.setEnabled(false);
                                    etPost.setEnabled(false);
                                    btUpload.setVisibility(View.INVISIBLE);
                                    btCancle.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });

                    btCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etPost.setText(post);
                            etPhoneNo.setText(phoneNo);
                            etPhoneNo.setEnabled(false);
                            etPost.setEnabled(false);
                            btUpload.setVisibility(View.INVISIBLE);
                            btCancle.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            });

        }

    }

    private void convertDataToString() {
        emp_no = tvEmpNo.toString();
        phoneNo = etPhoneNo.getText().toString();
        post = etPost.getText().toString();
    }

    private void UIcomponentInitilizer(View myProfile){
        tvEmpNo =(TextView) myProfile.findViewById(R.id.emp_no);
        tvUserName =(TextView)myProfile.findViewById(R.id.full_name);
        etPhoneNo =(EditText)myProfile.findViewById(R.id.mobile_no);
        etPost = (EditText)myProfile.findViewById(R.id.post);
        ivEdit = (ImageView)myProfile.findViewById(R.id.bt_edit);
        btUpload =myProfile.findViewById(R.id.profile_upload_button);
        btCancle = myProfile.findViewById(R.id.profile_cancle_button);
    }

    private void setData(){
        tvEmpNo.setText(emp_no);
        tvUserName.setText(details[0].toString());
        etPost.setText(details[2].toString());
        etPhoneNo.setText("0"+details[1].toString());
    }

    private class AsyncUpdateProfileDetails extends AsyncTask<String, String, String>
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

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where the php file resides
                url = new URL("http://100.64.183.49/webService/updateProfileDetails.inc.php");//TODO change IP address

            } catch (MalformedURLException e) {
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
                        .appendQueryParameter("post", params[1])
                        .appendQueryParameter("phone_no", params[2]);

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
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(getActivity(), "Successfully Updated!!", Toast.LENGTH_LONG).show();
            }else if (result.equalsIgnoreCase("false")){
                Toast.makeText(getActivity(), "Failed to Upload.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Failed to Upload.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }

            etPost.setText(update_post);
            etPhoneNo.setText(update_phoneNo);
            etPhoneNo.setEnabled(false);
            etPost.setEnabled(false);
            btUpload.setVisibility(View.INVISIBLE);
            btCancle.setVisibility(View.INVISIBLE);

        }

    }

}


