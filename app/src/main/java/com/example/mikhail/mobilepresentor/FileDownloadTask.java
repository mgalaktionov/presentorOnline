package com.example.mikhail.mobilepresentor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileDownloadTask extends AsyncTask<String, String, String> {


    public static Context context;
    private String title;
    private File File;
    private String pdfUrl;

    public FileDownloadTask(Context context, String title, String pdfUrl) {
        FileDownloadTask.context = context;
        this.pdfUrl = pdfUrl;
        this.title = title;
    }

    @Override
    protected String doInBackground(String[] urls) {

        DefaultHttpClient client = new DefaultHttpClient();
        String ret = "";
        for (String url : urls) {
            try {
                HttpGet getMethod = new HttpGet(url);
                HttpResponse response = client.execute(getMethod);
                InputStream inStream = response.getEntity().getContent();
                FileOutputStream fileWriter = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Download/" + title);
                int dataByte = inStream.read();
                while (dataByte > -1) {
                    fileWriter.write(dataByte);
                    dataByte = inStream.read();
                }
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    @Override
    protected void onPreExecute() {
        try {
            File = new File(Environment.getExternalStorageDirectory() + "/Download/" + title);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent(context, PDFRenderActivity.class);
        intent.putExtra("targetPDF", Environment.getExternalStorageDirectory()
                + "/Download/" + title);
        context.startActivity(intent);
    }


}
