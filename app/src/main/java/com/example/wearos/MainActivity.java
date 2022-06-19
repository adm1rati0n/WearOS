package com.example.wearos;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener eventListener;
    private ActivityMainBinding binding;
    private TextView txtX, txtY, txtZ;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txtY);
        txtZ = findViewById(R.id.txtZ);
        imgView = findViewById(R.id.img);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
        eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y, remappedRotationMatrix);
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }
                txtX.setText(String.format("X=%s", String.valueOf(((int) orientations[1]))));
                txtY.setText(String.format("Y=%s", String.valueOf(((int) orientations[2]))));
                txtZ.setText(String.format("Z=%s", String.valueOf(((int) orientations[0]))));
                imgView.setRotation(orientations[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(eventListener,sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(eventListener);
    }
}