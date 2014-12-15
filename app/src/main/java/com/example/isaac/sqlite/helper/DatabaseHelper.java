package com.example.isaac.sqlite.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.GpsStatus;
import android.util.Log;

import com.example.isaac.sqlite.model.PostHeader;
import com.example.isaac.sqlite.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaac on 12/6/2014.
 * copied from http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PostRequests";


    private static final String CREATE_TABLE_POST = "CREATE TABLE IF NOT EXISTS posts(" +
            "name TEXT PRIMARY KEY," +
            "url TEXT)";

    // Tag table create statement
    private static final String CREATE_TABLE_HEADER = "CREATE TABLE IF NOT EXISTS headers(" +
            "postname TEXT," +
            "key TEXT," +
            "value TEXT)";

    // todo_tag table create statement


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PostRequest getPost(String name) {

        String selectQuery = "SELECT key, value FROM headers h WHERE h.postname = '" + name + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT url FROM posts WHERE name = '" + name + "'",null);
        if(c.getCount()==0)
            return null;
        c.moveToFirst();
        String url = c.getString(c.getColumnIndex("url"));

        c = db.rawQuery(selectQuery, null);
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        int keycol = c.getColumnIndex("key");
        int valcol = c.getColumnIndex("value");
        if (c.moveToFirst()) {
            do {
                headers.add(new BasicNameValuePair(c.getString(keycol),c.getString(valcol)));
            } while (c.moveToNext());
        }
        return new PostRequest(url,name,headers);
    }

    public List<PostRequest> getAllPostRequests(){
        List<PostRequest> l = new ArrayList<PostRequest>();
        Cursor c = this.getReadableDatabase().rawQuery("SELECT name FROM posts", null);
        if (c.moveToFirst()) {
            do {
                l.add(getPost(c.getString(c.getColumnIndex("name"))));
            } while (c.moveToNext());
        }
        return l;
    }
    public PostRequest CreateInDatabase(String name, String url, List<NameValuePair> h) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("name", name);
        db.insert("posts",null,values);
        for(NameValuePair ph : h) {
            values = new ContentValues();
            values.put("postname", name);
            values.put("key",ph.getName());
            values.put("value",ph.getValue());
            db.insert("headers",null,values);
        }
        return getPost(name);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_HEADER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS headers");

        // create new tables
        onCreate(db);
    }
}