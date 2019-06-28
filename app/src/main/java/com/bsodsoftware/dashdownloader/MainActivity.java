package com.bsodsoftware.dashdownloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bsodsoftware.dashdownloader.services.DashDownloaderService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DashDownloaderService dashDownloaderService;
    private Button btnDownload;
    private EditText txtLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                txtLink.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }
    }

    private void init() {
        setServices();
        setControls();
    }

    private void setServices() {
        dashDownloaderService = new DashDownloaderService(this);
    }

    private void setControls() {
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
        txtLink = (EditText) findViewById(R.id.txtLink);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDownload:
                tryDownload();
                break;
        }
    }

    private void tryDownload() {
        try {
            String text = txtLink.getText().toString();
            if (text != null && !text.isEmpty()) {
                Toast.makeText(this, "Trying to Download Video..." , Toast.LENGTH_LONG).show();
                dashDownloaderService.process(text);
            } else {
                Toast.makeText(this, "Error: URL can't be empty!" , Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
