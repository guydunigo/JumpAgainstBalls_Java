package com.example.goni_colmaire.jumpagainstwalls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;

import java.util.Random;
import java.util.Vector;

/**
 * Created by gguy on 20/10/17.
 */

public class GameThread extends Thread {
    private BallPlayer ball;
    private Vector<Ball> balls;
    private GameView view;
    private boolean isRunning;
    public float accelX;
    public float accelY;
    private long offsetY;
    private Context context;
    private GameActivity activity;
    private boolean allSet;
    private Ball[] leftBalls;
    private Ball[] rightBalls;
    private boolean isDemo;

    public GameThread(GameActivity act, Context context, GameView v, BallPlayer b, Vector<Ball> balls) {
        super();
        ball = b;
        this.balls = balls;
        view = v;
        isRunning = false;
        accelX = 0;
        accelY = 0;
        offsetY = 0;
        this.activity = act;
        this.context = context;
        allSet = false;
        isDemo = false;
    }

    public GameThread(MainMenu act, Context context, GameView v, BallPlayer b, Vector<Ball> balls) {
        this((GameActivity) null, context, v, b, balls);
        isDemo = true;
    }

    public void start() {
        super.start();
        isRunning = true;
    }

    public void interrupt() {
        super.interrupt();
        isRunning = false;
    }

    private void updateSideBall(Ball[] b) {
        Ball topBall = b[0];
        Ball lowBall = b[0];

        for (int i = 0; i < b.length; i++) {
            if (b[i].y < topBall.y) {
                topBall = b[i];
            } else if (b[i].y > lowBall.y) {
                lowBall = b[i];
            }
        }

        if (!topBall.isVisible(view.height, offsetY) && lowBall.y < ball.y) {
            topBall.y = -offsetY + view.height + (new Random()).nextFloat() * view.height / 2.f;
        } else if (!lowBall.isVisible(view.height, offsetY) && topBall.y > ball.y) {
            lowBall.y = -offsetY - view.height / 2.f - (new Random()).nextFloat() * view.height / 2.f;
        }
    }

    public void run() {
        super.run();
        while (isRunning == true) {
            if (!isDemo && ball.nbLoops > Tools.MAX_LOOPS) {
                break;
            }

            if (view.width != 0 && view.height != 0) {
                // When we finally get the screen size,
                if (!allSet) {
                    leftBalls = new Ball[]{
                            new Ball(30, view.height / 3.f, 100, Color.GREEN),
                            new Ball(30, view.height * 4 / 3.f, 100, Color.BLUE)
                    };
                    rightBalls = new Ball[]{
                            new Ball(view.width - 30, view.height * 2 / 3.f, 100, Color.YELLOW),
                            new Ball(view.width - 30, view.height * 5 / 3.f, 100, Color.MAGENTA)
                    };
                    balls.clear();
                    balls.add(new Ball(view.width / 2.f, view.height + 2.9f * view.width, 3 * view.width, Color.RED));
                    for (int i = 0; i < leftBalls.length; i++) {
                        balls.add(leftBalls[i]);
                    }
                    for (int i = 0; i < rightBalls.length; i++) {
                        balls.add(rightBalls[i]);
                    }
                    allSet = true;
                }

                ball.step(Tools.STEPTIME, Tools.TIMEFACTOR, Tools.ACCEL_X_COEF * accelX, Tools.ACCEL_Y, view.width, view.height, balls);

                // update offset :
                if (ball.y + offsetY < view.height / 3.f) {
                    offsetY = (long) (view.height / 3.f - ball.y);
                } else if (ball.y + ball.radius + offsetY > 2 / 3.f * view.height) {
                    offsetY = (long) (2 / 3.f * view.height - ball.y - ball.radius);
                }

                // Move the side balls if necessary
                updateSideBall(leftBalls);
                updateSideBall(rightBalls);

                // Vibrate if another ball was touched
                if (!isDemo && ball.collided) {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    // v.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                }

                // Refresh the screen
                view.reDraw(offsetY);
            }

            try {
                sleep(Tools.STEPTIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // If the game is finished, show a popUp :
        if (!isDemo && activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Your dialog code.
                    // The game is over
                    AlertDialog.Builder dialogB = new AlertDialog.Builder(activity);
                    dialogB.setTitle("The game is over");
                    dialogB.setMessage("In " + ball.getTimeSeconds() + ", you jumped up to " + ball.getMaxHeight(view.height) + ".");
                    dialogB.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.reset();
                        }
                    });
                    dialogB.setNegativeButton("Back to menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity, MainMenu.class);
                            activity.startActivity(intent);
                        }
                    });
                    dialogB.setCancelable(false);
                    dialogB.show();
                }
            });
        }
    }
}
