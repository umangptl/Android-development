package com.example.androidactivitylifecycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int threadCounter = 0; // Global counter
    private TextView counterTextView; // To display the counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.counter_text_view);
        updateCounterDisplay();

        Button buttonB = findViewById(R.id.start_activity_b_btn);
        Button buttonC = findViewById(R.id.start_activity_c_btn);

        buttonB.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ActivityB.class);
            intent.putExtra("counterValue", threadCounter); // Pass counter value
            startActivityForResult(intent, 1); // Start Activity B
        });

        buttonC.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ActivityC.class);
            intent.putExtra("counterValue", threadCounter); // Pass counter value
            startActivityForResult(intent, 2); // Start Activity C
        });
    }

    // Handle result from Activity B and C
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("updatedCounter")) {
                threadCounter = data.getIntExtra("updatedCounter", 0); // Get updated counter value
                updateCounterDisplay(); // Update the counter display
            }
        }
    }

    // Function to update the counter display
    private void updateCounterDisplay() {
        counterTextView.setText("Thread Counter: " + threadCounter);
    }
}
