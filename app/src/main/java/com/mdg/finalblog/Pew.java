package com.mdg.finalblog;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mdg.finalblog.HttpHandler;
import com.mdg.finalblog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Pew extends AppCompatActivity {

    private String TAG = Pew.class.getSimpleName();

    private Handler handler=new Handler();
    private int count=0;


    // URL to get contacts JSON
    private static String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=UCq-Fj5jknLsUf-MWSy4_brA&fields=items/statistics/subscriberCount&key=AIzaSyDQ00u5qpb7deOQk2VnzCzacTt16NVDu3I";

    private static String url2 ="https://www.googleapis.com/youtube/v3/channels?part=statistics&id=UC-lHJZR3Gqxm24_Vd_AJ5Yw&fields=items/statistics/subscriberCount&key=AIzaSyDQ00u5qpb7deOQk2VnzCzacTt16NVDu3I";



    private  String url3="https://www.youtube.com/user/PewDiePie";
    private  String url4="https://www.youtube.com/user/tseries";
    private  String subscriberCount,subscriberCount2,difference;
    private TextView t1,t2,t3;
    private Button pewsub;
    private Button tsub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pew);


        t1=(TextView)findViewById(R.id.s1);
        t2=(TextView)findViewById(R.id.s2);
        t3=(TextView)findViewById(R.id.s3);
        pewsub=(Button)findViewById(R.id.button2);
        tsub=(Button)findViewById(R.id.button3);

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                new GetContacts().execute();
                handler.postDelayed(this,5);
            }
        };
        runnable.run();

        BottomNavigationView navigation =  findViewById(R.id.navigation);

        Menu menu= navigation.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_notifications:
                        Intent intent=new Intent(Pew.this,LoginActivity.class);
                        startActivity(intent);
                        break;

                }


                return false;
            }
        });

        pewsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pew=new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                startActivity(pew);
            }
        });

        tsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tseries=new Intent(Intent.ACTION_VIEW, Uri.parse(url4));
                startActivity(tseries);
            }
        });

    }








    /**
     * Async task class to get json by making HTTP call
     */



    private class GetContacts extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            HttpHandler sh2 = new HttpHandler();

            String[] jsonStr= new String[2];
            jsonStr[0] = sh.makeServiceCall(url2);
            jsonStr[1] = sh2.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + Arrays.toString(jsonStr));


            if (jsonStr[1] != null ) {
                try {
                    JSONObject jsonObj[] = new JSONObject[2];
                    jsonObj[0] = new JSONObject(jsonStr[1]);
                    jsonObj[1]= new JSONObject(jsonStr[0]);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj[0].getJSONArray("items");
                    JSONArray contacts2 = jsonObj[1].getJSONArray("items");
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        JSONObject c2 = contacts2.getJSONObject(i);


                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("statistics");
                        JSONObject phone2 = c2.getJSONObject("statistics");
                        subscriberCount = phone.getString("subscriberCount");
                        subscriberCount2 = phone2.getString("subscriberCount");


                        int sub1 = Integer.parseInt(subscriberCount2);
                        int sub2 = Integer.parseInt(subscriberCount);
                        int diff=sub1-sub2;
                        difference=String.valueOf(diff);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            /**
             * Updating parsed JSON data into ListView
             * */

            t1.setText(subscriberCount);
            t2.setText(subscriberCount2);
            t3.setText(difference);


        }

    }
}