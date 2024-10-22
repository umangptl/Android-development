package com.example.clap_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView proximityValueText;
    private TextView clapStatusText;
    private ImageView clapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews and ImageView
        proximityValueText = findViewById(R.id.proximity_value);
        clapStatusText = findViewById(R.id.clap_status);
        clapImage = findViewById(R.id.clap_image);

        // Set default image to single hand
        clapImage.setImageResource(R.drawable.single_hand);

        // Initialize SensorManager and proximity sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        if (proximitySensor == null) {
            proximityValueText.setText("Proximity sensor not available.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float proximityValue = event.values[0];

            // Display the proximity value
            proximityValueText.setText("Proximity: " + proximityValue);

            // Check if proximity sensor detects a "clap" (i.e., value is 0.0)
            if (proximityValue == 0.0) {
                // Change image to clap emoji and update status
                clapImage.setImageResource(R.drawable.clap_emoji);
                clapStatusText.setText("Clapping!");
            } else {
                // Change image back to single hand and reset status
                clapImage.setImageResource(R.drawable.single_hand);
                clapStatusText.setText("I’m ready to clap!");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}