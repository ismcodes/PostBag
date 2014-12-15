package com.example.isaac.sqlite.model;

import com.example.isaac.sqlite.helper.DatabaseHelper;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Isaac on 12/6/2014.
 */
public class PostRequest {
    private String url, name;
    private List<NameValuePair> headers;

    public PostRequest(String url, String name, List<NameValuePair> headers){
        this.headers = headers;
        this.url = url;
        this.name = name;
    }

    public String getUrl(){return url;}
    public String getName(){return name;}
    public List<NameValuePair> getHeaders(){return headers;}

    public String setUrl(String url){this.url=url; return url;}
    public String setName(String name){this.name=name; return name;}

    public static PostRequest create(DatabaseHelper dbh, String url, String name, List<NameValuePair> headers){
        return dbh.CreateInDatabase(url,name,headers);
        //creates new PostRequest with headers attached
    }
}
