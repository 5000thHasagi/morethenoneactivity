package com.example.morethenoneactivityapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetDataAsyncTask extends AsyncTask <String, Void, String> {

    public AsyncResponse delegate;
    private static final String TAG = "GetDataAsyncTask";

    public GetDataAsyncTask (AsyncResponse delegate){
        this.delegate = delegate;
    }
    public interface AsyncResponse{
        void proccessFinish (String output);
    }


    @Override
    protected String doInBackground(String... strings) {

        try {
            String login = strings[0];
            String password = strings[1];
            String url_Link = strings[2];

            String data = URLEncoder.encode("login", "UTF-8")+"="+URLEncoder.encode(login, "UTF-8");
            data+="&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");

            URL url = new URL(url_Link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);

            OutputStreamWriter writerOutputStream = new OutputStreamWriter(conn.getOutputStream());

            writerOutputStream.write(data);
            writerOutputStream.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sBuilder.append(line);
                break;
            }
            return sBuilder.toString();


        } catch (Exception e) {
            //e.printStackTrace();
            return new String("Exception "+e.getMessage());
        }
        //return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);
        if(result != null && !result.equals("")) {
            delegate.proccessFinish(result);
        }

    }
}
