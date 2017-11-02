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
        if (y + radius + offset < 0 || y - radius + offset > height) {
            return false;
        }
        return true;
    }

    protected static float getNorm(float[] vect) {
        return (float) Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1]);
    }

    protected static float[] getNormal(float[] pos1, float[] pos2) {
        float dx = pos2[0] - pos1[0];
        float dy = pos2[1] - pos1[1];

        return new float[]{dx, dy};
    }

    protected static float[] getUnitNormal(float[] pos1, float[] pos2) {
        float[] normal = getNormal(pos1, pos2);
        float norm = getNorm(normal);
        return new float[]{normal[0] / norm, normal[1] / norm};
    }

    protected static float[] getUnitNormal(float[] normal, float norm) {
        return new float[]{normal[0] / norm, normal[1] / norm};
    }
}
