package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Calendar;

import static com.example.ictassistant.myapplication.LoginActivity.CONNECTION_TIMEOUT;
import static com.example.ictassistant.myapplication.LoginActivity.READ_TIMEOUT;

/**
 * -------------------------------------------------------------------------------------------------
 * Created By   : Ms.K.A.H.Semini
 * Date         : 2017.10.13
 * Office       : IT Unit,Chief Secretary's Office, Galle
 * Purpose      : Handle request leave page.
 * -------------------------------------------------------------------------------------------------
 * Maintain Details
 *
 * No.     Date            Name                        Details
 * (01)    2017.10.13      Ms.K.A.H.Semini             Created
 * (02)    2017.11.02      Ms.K.A.H.Semini             Update request leave details
 * (03)    2017.11.26      Ms.K.A.H.Semini             Connected to the server DB
 * (04)    2017.12.09      Ms.K.A.H.Semini             Finalize the code in version 1.0
 * (05)
 * -------------------------------------------------------------------------------------------------
 */


public class leaveApplyFragment extends Fragment {

    Button bt_send,bt_cancel;
    Spinner sp_leave_type;
    public String emp_no;
    TextView tv_start_date,tv_end_date,tv_resuming_date;
    EditText et_no_of_days,et_leave_note;
    String leave_type,leave_note,start_date,end_date,resuming_date;
    String detailSet,name,post,phone_no;
    public String msg;
    int no_of_days;
    String[] details;// to store fetch profile data
    Calendar myCalendar = Calendar.getInstance();
    ArrayAdapter<String> adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emp_no = activity.getIntent().getStringExtra("emp_no");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View leaveApplyFragment = inflater.inflate(R.layout.fragment_leave_apply, container, false);
        UI_Initializer(leaveApplyFragment);
        String [] values ={"---Select Type---","Duty Leave","Personal Leave"};
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_leave_type.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                start_date =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                updateLabel(tv_start_date,start_date);
            }

        };

        tv_start_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date1,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                end_date =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                updateLabel(tv_end_date,end_date);
            }

        };

        tv_end_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date2,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                resuming_date =year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                updateLabel(tv_resuming_date,resuming_date);
            }

        };

        tv_resuming_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date3,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataConvertor();
                DataValidation();
                new leaveApplyFragment.AsyncGetChiefSecretaryPhoneNo().execute("1001");

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sp_leave_type.setAdapter(adapter);
                et_no_of_days.getText().clear();
                tv_start_date.setText(null);
                tv_end_date.setText(null);
                tv_resuming_date.setText(null);
                et_leave_note.getText().clear();
            }
        });

        return leaveApplyFragment;
    }

    public String CreateMessage(){
        String msg;
        new leaveApplyFragment.AsyncGetProfileDetails().execute(emp_no);
        msg = name+"("+post+") requests a "+leave_type + " from "+start_date+" to "+end_date+". Reason: "+leave_note+". For more detail: 0"+phone_no;
        return msg;
    }

    public void UI_Initializer(View leaveApplyFragment){
        bt_send = (Button) leaveApplyFragment.findViewById(R.id.leave_upload_button);
        bt_cancel =(Button) leaveApplyFragment.findViewById(R.id.leave_cancle_button);
        sp_leave_type = (Spinner) leaveApplyFragment.findViewById(R.id.leave_type);
        et_no_of_days = (EditText) leaveApplyFragment.findViewById(R.id.leave_days);
        tv_start_date = (TextView) leaveApplyFragment.findViewById(R.id.start_date);
        tv_end_date = (TextView) leaveApplyFragment.findViewById(R.id.end_date);
        tv_resuming_date = (TextView) leaveApplyFragment.findViewById(R.id.coming_date);
        et_leave_note = (EditText) leaveApplyFragment.findViewById(R.id.leave_note);
    }

    public void DataConvertor(){

        int i=sp_leave_type.getSelectedItemPosition();
        switch (i){
            case 1 : leave_type="duty leave" ;break;
            case 2 : leave_type="personal leave" ;break;
            default: leave_type="" ;break;
        }

        no_of_days = Integer.parseInt(et_no_of_days.getText().toString());
        start_date = tv_start_date.getText().toString();
        end_date = tv_end_date.getText().toString();
        resuming_date = tv_resuming_date.getText().toString();
        leave_note=et_leave_note.getText().toString();
        et_no_of_days.setEnabled(false);
        et_leave_note.setEnabled(false);
        et_leave_note.setCursorVisible(false);
        et_no_of_days.setCursorVisible(false);
    }

    private void updateLabel(TextView t,String s) {
        t.setText(s);
    }

    public void DataValidation(){

        if(leave_type.trim().length()<1||no_of_days<=0||start_date.trim().length()<1||end_date.trim().length()<1||resuming_date.trim().length()<1||leave_note.trim().length()<1){
            Toast.makeText(getActivity(), "Please fill all the fields... ", Toast.LENGTH_LONG).show();

            if(no_of_days<=0){
                et_no_of_days.setEnabled(true);
                et_no_of_days.setCursorVisible(false);
            }

            if(leave_note.trim().length()<1){
                et_leave_note.setCursorVisible(false);
                et_leave_note.setEnabled(true);
            }
        }
    }

    private class AsyncInsertLeave extends AsyncTask<String, String, String>
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
                url = new URL("http://100.64.183.49/webService/addLeaveRequest.inc.php");//TODO change IP address

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
                        .appendQueryParameter("leave_type", params[1])
                        .appendQueryParameter("no_of_days", params[2])
                        .appendQueryParameter("start_date",params[3])
                        .appendQueryParameter("end_date", params[4])
                        .appendQueryParameter("resuming_date", params[5])
                        .appendQueryParameter("duty_details",params[6]);

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
                Toast.makeText(getActivity(), "Request a leave successfully!!", Toast.LENGTH_LONG).show();
            }else if (result.equalsIgnoreCase("false")){
                Toast.makeText(getActivity(), "Failed to request a leave.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Failed to request a leave.Please Try again shortly...", Toast.LENGTH_LONG).show();
            }
            sp_leave_type.setAdapter(adapter);
            et_no_of_days.getText().clear();
            tv_start_date.setText(null);
            tv_end_date.setText(null);
            tv_resuming_date.setText(null);
            et_leave_note.getText().clear();
        }
    }

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
            //     pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
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
        }

        private void setData(){
            name=details[0].toString();
            post = details[2].toString();
            phone_no = details[1].toString();
        }

    }

    private class AsyncGetChiefSecretaryPhoneNo extends AsyncTask<String, String, String>
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
                url = new URL("http://100.64.183.49/webService/fetchChiefSecretaryPhoneNo.inc.php");//TODO change IP address

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

            String cheif_secretary_mobile_no = "+94"+result; //TODO update the chief sectary phone number
            Toast.makeText(getActivity(), cheif_secretary_mobile_no, Toast.LENGTH_LONG).show();
            try {
                msg =  CreateMessage();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(cheif_secretary_mobile_no, null, msg, null, null);

                new leaveApplyFragment.AsyncInsertLeave().execute(emp_no,leave_type,String.valueOf(no_of_days),start_date,end_date,resuming_date,leave_note);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
