package com.example.isaac.webtrigger;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.isaac.sqlite.helper.DatabaseHelper;
import com.example.isaac.sqlite.model.PostRequest;

import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends Activity {
private static DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //fixes AndroidBlockGuardPolicy for HTTP request

        }
        db = new DatabaseHelper(getApplicationContext());
        db.onCreate(this.openOrCreateDatabase("PostRequests", MODE_PRIVATE, null));
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);

        int prevId=-1;
        List<PostRequest> lpr = db.getAllPostRequests();
        for (int i = 0; i<lpr.size(); i++) {
            final PostRequest p = lpr.get(i);

            Button b = new Button(this);
            b.setText(p.getName());
            b.setId(i);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(prevId!=-1)
                lp.addRule(RelativeLayout.BELOW,prevId);
            prevId=i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text;
                    int duration = Toast.LENGTH_LONG;
                    // Create a new HttpClient and Post Header
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(p.getUrl());
                    try {
                            httppost.setEntity(new UrlEncodedFormEntity(p.getHeaders()));
                        HttpResponse response = httpclient.execute(httppost);
                        text = "Request sent to " + p.getUrl() + ", status code is " + response.getStatusLine().getStatusCode();
                    } catch (ClientProtocolException e) {
                        text = "Client Protocol Exception: " + e.getLocalizedMessage();
                    } catch (IOException e) {
                        text = "IO Exception: " + e.getLocalizedMessage();
                    }


                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();

                }
            });

            b.setLayoutParams(lp);
            layout.addView(b);
        }
    }

    public static DatabaseHelper getDB(){
        return db;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void navigateToNew(MenuItem m){
        Intent newPR = new Intent(this,NewPR.class);
        startActivity(newPR);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
