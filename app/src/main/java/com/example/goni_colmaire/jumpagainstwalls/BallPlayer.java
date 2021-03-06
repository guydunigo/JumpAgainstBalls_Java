package com.example.goni_colmaire.jumpagainstwalls;

import java.util.Vector;

/**
 * Created by gguy on 22/10/17.
 */

public class BallPlayer extends Ball {
    private float celX;
    private float celY;

    public boolean collided;
    private float maxHeight;
    public long nbLoops;

    public BallPlayer(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        this.maxHeight = y;
        this.collided = false;
        this.celX = 0;
        this.celY = 0;
        nbLoops = 0;
    }

    public String getRemainingTimeSeconds() {
        return String.valueOf((Tools.MAX_LOOPS - nbLoops) / 1000) + "s";
    }

    public String getTimeSeconds() {
        return String.valueOf(nbLoops / 1000) + "s";
    }

    public String getMaxHeight(float height) {
        return String.valueOf((long) -(this.maxHeight - height) / 10) + "m";
    }

    public String getHeight(float height) {
        return String.valueOf((long) -(y - height) / 10) + "m";
    }

    // Walls block the ball, balls are super bouncy
    public void step(float dt, float time_coef, float gravityX, float gravityY, float width, float height, Vector<Ball> balls) {
        if (width != 0 && height != 0) {
            nbLoops += dt;
            collided = false;

            dt *= time_coef;

            float[] gravity = { gravityX, gravityY };
            float[] normal = { 0, 0 };
            float[] unorm = { 0, 0 };
            float norm = 0;

            float[] pos = { x, y };
            float[] cel = { celX, celY };
            float[] new_pos = { x, y };
            float[] other_pos = { 0, 0 };
            float[] new_cel = { celX, celY };
            float tmp_cel = 0;

            int i = 0;

            Ball other;

            for (i = 0; i < 2; i++) {
                new_cel[i] = cel[i] + gravity[i] * dt;
                new_pos[i] = pos[i] + new_cel[i] * dt;
            }

            // Bouncing against other balls
            for (i = 0; i < balls.size(); i++) {
                other = balls.elementAt(i);
                other_pos = new float[] { other.x, other.y };
                normal = Tools.getVector(other_pos, new_pos);
                norm = Tools.getNorm(normal);
                float radSum = radius + other.radius;
                if (norm < radSum) {
                    unorm = Tools.getUnitVector(normal, norm);

                    tmp_cel = (-1) * Tools.BALLS_BOUNCE_COEF * (new_cel[0] * unorm[0] + new_cel[1] * unorm[1]);
                    for (i = 0; i < 2; i++) {
                        new_cel[i] = tmp_cel * unorm[i];
                        new_pos[i] += unorm[i] * 2 * (radSum - norm);
                    }

                    collided = true;
                }
            }

            // Colliding against walls
            if (new_pos[0] - radius < 0 || new_pos[0] + radius > width || new_pos[1] - radius < 0
                    || new_pos[1] + radius > height) {
                if (new_pos[0] - radius < 0) { // Left
                    new_pos[0] = radius;
                    new_cel[0] = 0;
                } else if (new_pos[0] + radius > width) { // Right
                    new_pos[0] = width - radius;
                    new_cel[0] = 0;
                }

                if (new_pos[1] + radius > height) { // Bottom
                    new_pos[1] = height - radius;
                    new_cel[1] = 0;
                }
            }

            // Nullify the celerity when too low
            for (i = 0; i < 2; i++) {
                if (Math.abs(new_cel[i]) <= Tools.LIMIT_CELL)
                    new_cel[i] = 0;
            }

            // Actually update values
            celX = new_cel[0];
            x = new_pos[0];
            celY = new_cel[1];
            y = new_pos[1];

            if (y < maxHeight) {
                maxHeight = y;
            }
        }
    }
}
