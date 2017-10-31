package com.example.goni_colmaire.jumpagainstwalls;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Vector;

public class MainMenu extends AppCompatActivity {
    private BallPlayer ball;
    private Vector<Ball> balls;
    private GameView view;
    private GameThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                startActivity(intent);
            }
        });

        ball = new BallPlayer(500, 500, 100, Color.CYAN);
        balls = new Vector<Ball>();

        view = (GameView) findViewById(R.id.gameview);
        view.ball = ball;
        view.balls = balls;
        view.isDemo = true;
        thread = new GameThread(this, getApplicationContext(), view, ball, balls);
        thread.accelX = 0;
        thread.accelY = 10;
    }


    @Override
    protected void onResume() {
        super.onResume();
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }
}
