package com.example.goni_colmaire.jumpagainstwalls;

/**
 * Created by gguy on 20/10/17.
 */
public class Ball {
    public float radius;
    public float x;
    public float y;
    public int color;

    public Ball(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public boolean isVisible(float height, long offset) {
        return y + radius + offset > 0 && y - radius + offset < height;
    }
}
