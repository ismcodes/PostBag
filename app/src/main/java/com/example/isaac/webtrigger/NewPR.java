package com.example.isaac.webtrigger;
import com.example.isaac.sqlite.model.PostRequest;
import com.example.isaac.webtrigger.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NewPR extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pr);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public int getNumberOfHeadersInLayout(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.newPR);
        int count = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if(v instanceof EditText)
                count++;
        }
        return (count-2)/2;


    }
    public void addHeader(View v){
        //add another header key and value to the layout
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.newPR);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText key = new EditText(this);
        key.setText("Key");
        key.setId(View.generateViewId());
        View vi = layout.getChildAt(layout.getChildCount() - 1);
        int id = (layout.getChildCount()>3)?vi.getId():layout.getChildAt(1).getId();
        lp.addRule(RelativeLayout.BELOW,id);
        lp.addRule(RelativeLayout.ALIGN_PARENT_START,1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_END,1);
        key.setLayoutParams(lp);
        layout.addView(key);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText val = new EditText(this);
        val.setText("Value");
        val.setId(View.generateViewId());
        lp.addRule(RelativeLayout.BELOW, key.getId());
        lp.addRule(RelativeLayout.ALIGN_PARENT_START,1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_END,1);
        val.setLayoutParams(lp);
        layout.addView(val);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, val.getId());
        lp.addRule(RelativeLayout.ALIGN_PARENT_START,1);
        layout.findViewById(R.id.button_header_add).setLayoutParams(lp);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW,R.id.button_header_add);
        layout.findViewById(R.id.button_header_delete).setLayoutParams(lp);
    }
    public void deleteHeader(View v){
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.newPR);
        List<View> inputs = new ArrayList<View>();
        for(int i = 0; i < rl.getChildCount(); i++){
            if(rl.getChildAt(i) instanceof EditText)
                inputs.add(rl.getChildAt(i));
        }
        if(inputs.size()>=4) {
            int s = inputs.size();
            rl.removeView(inputs.get(s-1));
            rl.removeView(inputs.get(s-2));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, inputs.get(s-3).getId());
            lp.addRule(RelativeLayout.ALIGN_PARENT_START,1);
            rl.findViewById(R.id.button_header_add).setLayoutParams(lp);
        }
    }
    public void createPostRequestFromForm(MenuItem m){
        //TODO collect data from form, then use DBHelper to create it in DB & add to list in other activity
        //pop up error if duplicate key in headers or duplicate name of post request
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.newPR);
        String url = ((EditText)layout.getChildAt(0)).getText().toString();
        String name = ((EditText)layout.getChildAt(1)).getText().toString();
        //check before db add if name is not unique
        for(int i = 2; i<layout.getChildCount()-2; i+=2)
            if(layout.getChildAt(i) instanceof EditText && layout.getChildAt(i+1) instanceof EditText) {
                String hname = ((EditText)layout.getChildAt(i)).getText().toString();
                String hval = ((EditText)layout.getChildAt(i+1)).getText().toString();

                for (int j = 0; j < headers.size(); j++) {
                    if(headers.get(j).getName() == hname){
                        Toast toast = Toast.makeText(getApplicationContext(), "Header key name not unique for this PR", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
            }

        PostRequest.create(MainActivity.getDB(),url,name,headers);
        NavUtils.navigateUpFromSameTask(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_pr, menu);
        return true;
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
        else if (id==android.R.id.home) {
            // Respond to the action bar's Up/Home button

            NavUtils.navigateUpFromSameTask(this);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
