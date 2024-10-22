package com.example.weblinkandphonecallapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Constants
    private static final int CALL_PHONE_PERMISSION = 100;
    private static final String EMPTY_URL_MESSAGE = "Enter a URL";
    private static final String INVALID_PHONE_MESSAGE = "Enter a valid phone number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Web Link and Phone Call Activity");
    }

    // Check phone permission when activity starts
    @Override
    protected void onStart() {
        super.onStart();
        checkPhonePermission();
    }

    private void checkPhonePermission() {
        int callPhonePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        if (callPhonePerm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION);
        }
    }

    // Close the app when close button is clicked
    public void closeApp(View view) {
        finish();
    }

    // Open browser when the "Launch URL" button is clicked
    public void openBrowser(View view) {
        EditText urlEditText = findViewById(R.id.editTextUrl);
        String url = urlEditText.getText().toString();
        if (!url.isEmpty()) {
            startBrowserActivity(url);
        } else {
            showToast(EMPTY_URL_MESSAGE);
        }
    }

    private void startBrowserActivity(String url) {
        if (!url.startsWith("http")) url = "https://" + url;
        Uri targetPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, targetPage);
        startActivity(intent);
    }

    // Open dialer when the "Call" button is clicked
    public void openDialer(View view) {
        EditText phoneEditText = findViewById(R.id.editTextPhone);
        String phoneNumber = phoneEditText.getText().toString();
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            startDialerActivity(phoneNumber);
        } else {
            showToast(INVALID_PHONE_MESSAGE);
        }
        Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse(phoneNumber));
        startActivity(implicit);
    }

    private void startDialerActivity(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    // Show a reusable toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Lifecycle methods
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
