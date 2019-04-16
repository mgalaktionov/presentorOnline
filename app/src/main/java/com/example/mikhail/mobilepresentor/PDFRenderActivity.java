package com.example.mikhail.mobilepresentor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class PDFRenderActivity extends AppCompatActivity {

    private boolean running;
    private int seconds;
    private String targetPdf;
    private int currentPage = 0;
    private int pageCount;
    private ImageView pdfView;
    private String code;
    private TextView slidesOfTotal;
    private TextView timer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfrender);
        Intent intent = getIntent();
        running = true;
        runTimer();
        code = intent.getStringExtra("code");
        targetPdf = intent.getStringExtra("targetPDF");
        pdfView = (ImageView) findViewById(R.id.pdfview);
        slidesOfTotal = (TextView) findViewById(R.id.slidesOfTotal);
        timer = (TextView) findViewById(R.id.timer);
        slidesOfTotal.setText(currentPage + 1 + "/" + pageCount);
        try {
            openPDF(currentPage);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Something Wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openPDF(int index) throws IOException {

        File file = new File(targetPdf);
        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);
        pageCount = pdfRenderer.getPageCount();
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(currentPage);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(rendererPageWidth, rendererPageHeight, Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfView.setImageBitmap(bitmap);
        rendererPage.close();
        pdfRenderer.close();
        fileDescriptor.close();

        slidesOfTotal.setText(currentPage + 1 + "/" + pageCount);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) throws IOException {
        if (currentPage == pageCount - 1) {
            Toast.makeText(this, "you're at the end", Toast.LENGTH_SHORT).show();
        } else {
            PageNumSendTask pageNumSendTask = new PageNumSendTask(code, currentPage);
            pageNumSendTask.execute();
            currentPage++;
            try {
                openPDF(currentPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onPrevClick(View v) throws IOException {
        if (currentPage == 0) {
            Toast.makeText(this, "you're at the begining", Toast.LENGTH_SHORT).show();
        } else {
            PagePrevNumSendTask pagePrevNumSendTask = new PagePrevNumSendTask(code, currentPage);
            pagePrevNumSendTask.execute();
            currentPage--;
            try {
                openPDF(currentPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runTimer() {
        final TextView textView = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 36000;
                int minutes = (seconds % 36000) / 60;
                int secnds = seconds % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, secnds);
                textView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}
