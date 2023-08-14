package com.example.stepcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.stepcounter.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor stepSensor=null;
    private int totalSteps=0;
    private  int previousTotalStep=0;
    private ProgressBar progressBar;
    private TextView steps;

    protected void onResume() {
        super.onResume();

        if (stepSensor==null){
            Toast.makeText(this, "SENSOR NOT FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            mSensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        progressBar=findViewById(R.id.progressBar);
        steps=findViewById(R.id.steps);

        resetSteps();
        loadData();

        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

    }


    protected  void onPaused(){
        super.onPause();
        mSensorManager.unregisterListener(this);

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            totalSteps= (int) event.values[0];
            int currentSteps=totalSteps-previousTotalStep;
            steps.setText(String.valueOf(currentSteps));
            progressBar.setProgress(currentSteps);
        }
        
    }

    private void resetSteps(){
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Long press to reset steps", Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                previousTotalStep=totalSteps;
                steps.setText("0");
                progressBar.setProgress(0);
                saveData();

                return true;
            }


        });
    }

    private void saveData(){
        SharedPreferences sharedPref=getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString("key1",String.valueOf(previousTotalStep));
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPref=getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedNumber = (int)sharedPref.getFloat("key1", 0f);
        previousTotalStep=savedNumber;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
