package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Ball01_BallPark4 extends Tray {

    // The game objects
    private BallPark white, red, green, blue;

    final float SPEED_THRESHOLD = 3f;
    final float DISTANCE_THRESHOLD = 1f;

    public Ball01_BallPark4(Start game) {
        super(game);

        white = new BallPark(world, new Vector2(-7f, -7f), 1f, Color.WHITE, new Vector2(7,7));
        red = new BallPark(world, new Vector2(7f, -7f), 1f, Color.RED, new Vector2(-7,7));
        green = new BallPark(world, new Vector2(7f, 7f), 1f, Color.GREEN, new Vector2(-7,-7));
        blue = new BallPark(world, new Vector2(-7f, 7f), 1f, Color.BLUE, new Vector2(7,-7));
    }

    @Override
    public void render(float delta) {

        switch (gameState) {

            case INSTRUCTIONS:
                displayLevelName();
                instructions(500);
                break;

            case RUNNING:
                drawTray(delta);

                if (nearEnough(white) && nearEnough(red) && nearEnough(green) && nearEnough(blue)) {
                    finishTimeSeconds = (int) ((System.currentTimeMillis() - startTimeMilliseconds) / 1000);
                    updateHighScore(finishTimeSeconds);
                    gameState = GameState.FINISHED;
                    break;
                }
                draw();
                physics();
                break;

            case FINISHED:
                displayFinalScore(delta);
                draw();
                break;

            case NEXT:
                Utils.nextScreen(this, topLevel);
                break;
        }
    }

    void instructions(int yPos) {
        spriteBatch.begin();
        font.draw(spriteBatch, "Park the 4 balls.", 200, yPos);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        red.move(g);
        green.move(g);
        blue.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        // Draw hole
        Utils.pixel(worldToPixel(white.hole), white.color, 10f);
        Utils.pixel(worldToPixel(red.hole), red.color, 10f);
        Utils.pixel(worldToPixel(green.hole), green.color, 10f);
        Utils.pixel(worldToPixel(blue.hole), blue.color, 10f);

        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        modelBatch.render(red.modelInstance, environment);
        modelBatch.render(green.modelInstance, environment);
        modelBatch.render(blue.modelInstance, environment);
        modelBatch.end();
    }


    private boolean nearEnough(BallPark ball) {
        float distanceFromGoal = ball.hole.cpy().sub(ball.box2dBody.getPosition()).len();
        float relVelToGoal = ball.box2dBody.getLinearVelocity().len();

        boolean relativelyNear = distanceFromGoal < DISTANCE_THRESHOLD;

        boolean relativelySlow = relVelToGoal < SPEED_THRESHOLD;

        return relativelyNear && relativelySlow;
    }
}
