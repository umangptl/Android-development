package com.example.servicesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("PDF Download Activity");
    }

    // Trigger the download service when the button is clicked
    public void startDownloadService(View view) {
        String[] pdfUrls = new String[5];

        pdfUrls[0] = ((EditText) findViewById(R.id.pdf1_input)).getText().toString();
        pdfUrls[1] = ((EditText) findViewById(R.id.pdf2_input)).getText().toString();
        pdfUrls[2] = ((EditText) findViewById(R.id.pdf3_input)).getText().toString();
        pdfUrls[3] = ((EditText) findViewById(R.id.pdf4_input)).getText().toString();
        pdfUrls[4] = ((EditText) findViewById(R.id.pdf5_input)).getText().toString();

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("pdfUrls", pdfUrls);
        startService(intent);
    }
}
