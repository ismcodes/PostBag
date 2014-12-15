package com.example.isaac.sqlite.model;
/**
 * Created by Isaac on 12/6/2014.
 */
public class PostHeader {


    private String postname, key, value;

    public PostHeader(String postname, String key, String value){
        this.postname=postname;
        this.key = key;
        this.value = value;
    }

    public String getKey(){return key;}
    public String getValue(){return value;}

    public String setValue(String value){this.value=value; return value;}
}
