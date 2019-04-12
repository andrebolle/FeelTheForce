package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Beam01_WhiteBall extends Tray {

    // The game objects
    private BallOnSpring white;
    private Beam beam;

    Vector2 whiteGoalPos = new Vector2(0f, 0f);
    Vector2 whiteGoalVel = new Vector2(0f, 0f);

    public Beam01_WhiteBall(Start game) {
        super(game);
        beam = new Beam(world, BEAM_LENGTH, BEAM_WIDTH, BEAM_DEPTH, BEAM_ANGULAR_VELOCITY);
        white = new BallOnSpring(world, new Vector2(0f, BEAM_LENGTH / 2f), WHITE_RADIUS, Color.WHITE);
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
                if (whiteNearEnough()) {
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
        font.draw(spriteBatch, "Read well, and remember this ...", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "Get the white ball to white EDGE of the beam.", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "When the white ball is RESTING on white edge,", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "HOLD the device FLAT and STILL.", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "Then WAIT, until the ball comes to REST.", 200, yPos);

        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {

        perspCam.position.set(0, -H, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

        Utils.line(worldToPixel(center), worldToPixel(white.box2dBody.getPosition()), Color.DARK_GRAY, 5f);

        modelBatch.begin(perspCam);
        modelBatch.render(beam.modelInstance, environment);
        modelBatch.render(white.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        modelBatch.end();

        perspCam.position.set(0, 0, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();
    }

    boolean whiteNearEnough() {

        final float SPEED_THRESHOLD = 3f;
        final float DISTANCE_THRESHOLD = WHITE_RADIUS;

        float beamAngle = beam.box2dBody.getAngle();

        whiteGoalPos.x = (float) Math.cos((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS);
        whiteGoalPos.y = (float) Math.sin((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS);

        whiteGoalVel.x = (float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY;
        whiteGoalVel.y = (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY;

        float whiteDistanceFromGoal = whiteGoalPos.sub(white.box2dBody.getPosition()).len();

        float whiteRelVelToGoal = white.box2dBody.getLinearVelocity().sub(whiteGoalVel).len();

        boolean whiteRelativelyNear = whiteDistanceFromGoal < DISTANCE_THRESHOLD;

        boolean whiteRelativelySlow = whiteRelVelToGoal < SPEED_THRESHOLD;

        return whiteRelativelyNear && whiteRelativelySlow;
    }


}
