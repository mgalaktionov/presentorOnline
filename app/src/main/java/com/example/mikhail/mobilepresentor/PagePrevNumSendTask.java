package com.example.mikhail.mobilepresentor;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class PagePrevNumSendTask extends AsyncTask<Integer, String, String> {
    private Integer pageNumber;
    private String code;
    public PagePrevNumSendTask(String code, Integer pageNumber) {
        this.pageNumber = pageNumber;
        Log.i(MainActivity.class.toString(), "PageNumSendTask instance created");
    }

    @Override
    protected String doInBackground(Integer... pages) {
        HttpClient httpClient = new DefaultHttpClient();
        try {

            HttpPost request = new HttpPost("http://185.117.152.219:6969/page");
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("login", code);
            jsonObject.accumulate("page", pageNumber);
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");

            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            Log.i(MainActivity.class.toString(), response.toString());
            //handle response here...

        } catch (Exception ex) {
            Log.i(MainActivity.class.toString(), "PIZDA RUL'U I SEDLU");
            ex.printStackTrace();
            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        Log.i(MainActivity.class.toString(), "STARTED");
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(MainActivity.class.toString(), "ENDED");
    }


}
