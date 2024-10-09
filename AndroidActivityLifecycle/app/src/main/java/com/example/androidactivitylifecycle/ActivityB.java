package com.example.androidactivitylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityB extends AppCompatActivity {

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        // Get the counter value from MainActivity
        Intent intent = getIntent();
        counter = intent.getIntExtra("counterValue", 0);

        // Increment the counter by 5
        counter += 5;

        // Return the updated counter to MainActivity
        Button returnButton = findViewById(R.id.return_to_main_btn);
        returnButton.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedCounter", counter);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close Activity B
        });
    }
}
