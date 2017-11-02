package com.example.goni_colmaire.jumpagainstwalls;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Vector;

/**
 * Created by gguy on 20/10/17.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public BallPlayer ball;
    public Vector<Ball> balls;
    public int width;
    public int height;
    public boolean isDemo;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void reDraw(long offsetY) {
        if (ball != null) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (getHolder()) {
                    Paint p = new Paint();
                    p.setColor(ball.color);
                    // Clear canvas
                    canvas.drawColor(Color.BLACK);
                    // Draw player ball
                    canvas.drawCircle(
                            ball.x,
                            ball.y + offsetY,
                            ball.radius,
                            p
                    );
                    // Draw other balls
                    for (int i = 0; i < balls.size(); i++) {
                        if (balls.elementAt(i).isVisible(height, offsetY)) {
                            p.setColor(balls.elementAt(i).color);
                            canvas.drawCircle(
                                    balls.elementAt(i).x,
                                    balls.elementAt(i).y + offsetY,
                                    balls.elementAt(i).radius,
                                    p
                            );
                        }
                    }
                    // Draw stats
                    if (!isDemo) {
                        TextPaint tp = new TextPaint();
                        tp.setTextSize(100);
                        tp.setColor(Color.WHITE);
                        canvas.drawText(ball.getRemainingTimeSeconds(), 10, 100, tp);
                        tp.setColor(Color.rgb(150, 150, 0));
                        canvas.drawText(ball.getMaxHeight(height), 10, 200, tp);
                        tp.setColor(Color.CYAN);
                        canvas.drawText(ball.getHeight(height), 10, 300, tp);
                    }
                }

                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
