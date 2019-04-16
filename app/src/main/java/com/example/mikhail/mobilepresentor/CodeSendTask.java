package com.example.mikhail.mobilepresentor;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class CodeSendTask extends AsyncTask<String, String, String> {
    private String code;

    public CodeSendTask(String code) {
        this.code = code;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://185.117.152.219:6969/login");
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("login", code);
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            Log.i(MainActivity.class.toString(), response.toString());
        } catch (Exception ex) {
            Log.i(MainActivity.class.toString(), "PIZDA RUL'U I SEDLU");
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(MainActivity.class.toString(), "STARTED");
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(MainActivity.class.toString(), "ENDED");
    }
}
