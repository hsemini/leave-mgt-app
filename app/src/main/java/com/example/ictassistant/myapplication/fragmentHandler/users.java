package com.example.ictassistant.myapplication.fragmentHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class users extends Fragment {

    ListView myList;
    String emp_no;
    ArrayList<HashMap<String, String>> nameList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emp_no = activity.getIntent().getStringExtra("emp_no");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View users = inflater.inflate(R.layout.fragment_users, container, false);

        myList = users.findViewById(R.id.nameList1);
        nameList = new ArrayList<>();

        new users.AsyncGetFullNameList().execute(emp_no);

        return users;
    }

    private class AsyncGetFullNameList extends AsyncTask<String, String, String>
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
                url = new URL("http://100.65.235.119/webService/fetchFullNames.inc.php");//TODO change IP address

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

                                String full_name= c.getString("full_name");
                                //String start_date = c.getString("start_date");
                                //String end_date = c.getString("end_date");
                                //String duty_details = c.getString("duty_details");

                                // tmp hash map for single contact
                                HashMap<String, String> names = new HashMap<>();

                                // adding each child node to HashMap key => value
                                names.put("full_name", full_name);
                                //leave.put("start_date", start_date);
                                //leave.put("end_date", end_date);
                                //leave.put("duty_details", duty_details);
                                nameList.add(names);

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
                    getActivity(), nameList,
                    R.layout.row_format_name_list, new String[]{"full_name"}, new int[]{
                    R.id.names});

            myList.setAdapter(adapter);
            myList.setClickable(true);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
                {
                    String item1 = myList.getItemAtPosition(position).toString();
                    String item2[] = item1.toString().split("full_name=");
                    item2[1]=item2[1].substring(0,item2[1].length()-1);

                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction FT = fragmentManager.beginTransaction();
                    showLeaveHistoryFragment slhf = new showLeaveHistoryFragment();

                    Bundle args = new Bundle();
                    args.putString("full_name", item2[1].trim().toString());
                    slhf.setArguments(args);

                    FT.replace(R.id.fragmentHandler, slhf);
                    FT.commit();
                }
            });

        }

    }

}
