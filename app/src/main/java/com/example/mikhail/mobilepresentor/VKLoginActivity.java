package com.example.mikhail.mobilepresentor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class VKLoginActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pDialog;
    private ListView VKDocsListView;
    private String[] scope = new String[]{VKScope.DOCS};
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> URLs = new ArrayList<>();
    private HashMap<Integer, String> vkDocuments = new HashMap<>();
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vklogin);
        VKSdk.login(this, scope);
        context = getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Authorisation was sucsesfull", Toast.LENGTH_SHORT).show();
                VKDocsListView = (ListView) findViewById(R.id.VK_docs_ListView);
                VKRequest request = new VKRequest("docs.get");
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        String responseAsString = (String) response.responseString;
                        JSONObject responseAsJSON = response.json;
                        JSONObject parsedResponseAsJSON = null;
                        JSONArray items = null;
                        try {
                            parsedResponseAsJSON = responseAsJSON.getJSONObject("response");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            items = parsedResponseAsJSON.getJSONArray("items");
                            System.out.println(items.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        try {
                            System.out.println(items.get(0).getClass());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int pos = 0;
                        for (int i = 0; i < items.length(); i++) {

                            try {
                                JSONObject temp = items.getJSONObject(i);
                                if (temp.getString("title").contains(".pdf") || temp.getString("title").contains(".pptx")) {
                                    titles.add(temp.getString("title"));
                                    URLs.add(temp.getString("url"));

                                    vkDocuments.put(pos, temp.getString("url"));
                                    pos++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VKLoginActivity.this, android.R.layout.simple_expandable_list_item_1, titles);
                        VKDocsListView.setAdapter(arrayAdapter);
                        VKDocsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                File file = new File(Environment.getExternalStorageDirectory()
                                        + "/Download/" + titles.get(position));
                                if (file.exists()) {
                                    URLSendTask urlSendTask2 = new URLSendTask(code,vkDocuments.get(position));
                                    urlSendTask2.execute();
                                    Intent intent = new Intent(context, PDFRenderActivity.class);
                                    intent.putExtra("targetPDF", Environment.getExternalStorageDirectory()
                                            + "/Download/" + titles.get(position));
                                    context.startActivity(intent);
                                } else {
                                    ProgressBar progressBar;
                                    URLSendTask urlSendTask2 = new URLSendTask(code,vkDocuments.get(position));
                                    urlSendTask2.execute();
                                    FileDownloadTask fileDownloadTask = new FileDownloadTask(context, titles.get(position), vkDocuments.get(position));
                                    fileDownloadTask.execute(new String[]{vkDocuments.get(position)});

                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "Authorisation faild", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
