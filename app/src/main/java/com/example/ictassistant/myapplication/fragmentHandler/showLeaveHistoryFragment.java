package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.ictassistant.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.example.ictassistant.myapplication.LoginActivity.CONNECTION_TIMEOUT;
import static com.example.ictassistant.myapplication.LoginActivity.READ_TIMEOUT;

public class showLeaveHistoryFragment extends Fragment {

    String full_name,emp_no;
    ArrayList<HashMap<String, String>> LeaveList;
    private ListView list;
    Button btBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View showLeaveHistoryFragment = inflater.inflate(R.layout.fragment_show_leave_history, container, false);

        LeaveList = new ArrayList<>();

        list = showLeaveHistoryFragment.findViewById(R.id.leaveList);
        btBack = showLeaveHistoryFragment.findViewById(R.id.btBack);

        full_name =getArguments().getString("full_name").trim().toString();
        // Toast.makeText(getActivity(), full_name, Toast.LENGTH_LONG).show();

        new showLeaveHistoryFragment.AsyncGetEmpNoFromFullName().execute(full_name);

        return showLeaveHistoryFragment;
    }

    private class AsyncGetEmpNoFromFullName extends AsyncTask<String, String, String>
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
                url = new URL("http://100.65.235.119/webService/fetchEmpNoFromFullName.inc.php");//TODO change IP address

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
                        .appendQueryParameter("full_name", params[0]);
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

            emp_no = result;
            //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            new showLeaveHistoryFragment.AsyncLeaveDetails().execute(emp_no);
        }

    }

    private class AsyncLeaveDetails extends AsyncTask<String, String, String>
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
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://100.65.235.119/webService/fetchLeaves.inc.php");//TODO change IP address

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

                    //TODO set data
                    String jsonStr = result.toString();
                    jsonStr = jsonStr.substring(1,jsonStr.length()-1);

                    //--------------
                    if(jsonStr!=null){
                        try {
                            JSONArray jsonArr = new JSONArray(result.toString());
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject c = jsonArr.getJSONObject(i);

                                String leave_type= c.getString("leave_type");
                                String start_date = c.getString("start_date");
                                String end_date = c.getString("end_date");
                                String duty_details = c.getString("duty_details");

                                // tmp hash map for single contact
                                HashMap<String, String> leave = new HashMap<>();

                                // adding each child node to HashMap key => value
                                leave.put("leave_type", leave_type);
                                leave.put("start_date", start_date);
                                leave.put("end_date", end_date);
                                leave.put("duty_details", duty_details);
                                LeaveList.add(leave);

                            }

                        }catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                        }
                    }

                    return null;
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

            pdLoading.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), LeaveList,
                    R.layout.row_format_my_claim, new String[]{"leave_type","start_date","end_date","duty_details"}, new int[]{
                    R.id.leave_type, R.id.start_day, R.id.end_day, R.id.leave_details});

            list.setAdapter(adapter);

            btBack.setVisibility(View.VISIBLE);

            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();
                    users users = new users();

                    FT.replace(R.id.fragmentHandler, users);
                    FT.commit();

                }
            });

        }

    }


}
