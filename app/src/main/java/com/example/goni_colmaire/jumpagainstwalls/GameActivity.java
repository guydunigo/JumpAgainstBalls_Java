package com.example.goni_colmaire.jumpagainstwalls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Vector;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView view;
    private BallPlayer ball;
    private Vector<Ball> balls;
    private GameThread thread;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ball = new BallPlayer(500, 500, 100, Color.CYAN);
        balls = new Vector<Ball>();

        view = (GameView) findViewById(R.id.gameview);
        view.ball = ball;
        view.balls = balls;
        view.isDemo = false;
        thread = new GameThread(this, getApplicationContext(), view, ball, balls);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        thread.accelX = event.values[0];
        thread.accelY = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        thread.start();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
        mSensorManager.unregisterListener(this);
    }

    public void reset() {
        Intent intent = this.getIntent();
        this.finish();
        this.startActivity(intent);
    }
}
