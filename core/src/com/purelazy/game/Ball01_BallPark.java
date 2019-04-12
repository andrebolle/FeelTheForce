package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

// Park the ball in its own spot.
public class Ball01_BallPark extends Tray {

    // The game objects
    private BallPark white;
    private Vector2 hole = new Vector2(0,0);

    final float SPEED_THRESHOLD = 3f;
    final float DISTANCE_THRESHOLD = 1f;

    public Ball01_BallPark(Start game) {
        super(game);

        white = new BallPark(world, new Vector2(-5f, -5f), 1f, Color.WHITE, hole);
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

                if (nearEnough()) {
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
        font.draw(spriteBatch, "Park the ball in its own spot.", 200, yPos);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        // Draw hole
        Utils.pixel(worldToPixel(hole), Color.WHITE, 10f);

        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        modelBatch.end();
    }


    private boolean nearEnough() {
        float whiteDistanceFromGoal = hole.cpy().sub(white.box2dBody.getPosition()).len();
        float whiteRelVelToGoal = white.box2dBody.getLinearVelocity().len();

        boolean whiteRelativelyNear = whiteDistanceFromGoal < DISTANCE_THRESHOLD;

        boolean whiteRelativelySlow = whiteRelVelToGoal < SPEED_THRESHOLD;

        return whiteRelativelyNear && whiteRelativelySlow;
    }
}
