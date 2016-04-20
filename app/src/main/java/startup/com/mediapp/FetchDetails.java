package startup.com.mediapp;

import android.content.Context;

/**
 * Created by Harshil on 19/04/2016.
 */
public class FetchDetails {

    String email,password,url;
    Context c;


    public  FetchDetails(String e, String p, String url, Context c, String u){
        this.c = c;
        this.email = e;
        this.password = p;
        this.url = url;


    }

    public void fetch(){

    }

}
