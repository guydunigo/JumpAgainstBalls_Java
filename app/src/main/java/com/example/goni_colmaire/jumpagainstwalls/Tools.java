package com.example.goni_colmaire.jumpagainstwalls;

/**
 * Created by gguy on 04/11/17.
 */

public final class Tools {
    public static final int STEPTIME = 20;
    public static final int ACCEL_Y = 10;
    public static final int ACCEL_X_COEF = -3;

    public static final long MAX_LOOPS = 30000;
    public static final float TIMEFACTOR = 1 / 50.f;
    public static final int LIMIT_CELL = 1;
    public static final float BALLS_BOUNCE_COEF = 1.5f;

    public static float getNorm(float[] vect) {
        return (float) Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1]);
    }

    public static float[] getVector(float[] pos1, float[] pos2) {
        float dx = pos2[0] - pos1[0];
        float dy = pos2[1] - pos1[1];

        return new float[]{dx, dy};
    }

    public static float[] getUnitVector(float[] pos1, float[] pos2) {
        float[] normal = getVector(pos1, pos2);
        float norm = getNorm(normal);
        return new float[]{normal[0] / norm, normal[1] / norm};
    }

    public static float[] getUnitVector(float[] normal, float norm) {
        return new float[]{normal[0] / norm, normal[1] / norm};
    }
}
