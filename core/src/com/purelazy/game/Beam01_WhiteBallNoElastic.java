package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Beam01_WhiteBallNoElastic extends Tray {

    // The game objects
    private BallOnSpring white;
    private Beam beam;
    // Particle effect
    //private ParticleEffect fire;

    Vector2 whiteGoalPos = new Vector2(0f, 0f);
    Vector2 whiteGoalVel = new Vector2(0f, 0f);

    public Beam01_WhiteBallNoElastic(Start game) {
        super(game);
        buildScene();
    }

    private void buildScene() {
        // Create beam
        beam = new Beam(world, BEAM_LENGTH, BEAM_WIDTH, BEAM_DEPTH, BEAM_ANGULAR_VELOCITY);

        // Create balls
        white = new BallOnSpring(world, new Vector2(0f, BEAM_LENGTH / 2f), WHITE_RADIUS, Color.WHITE);

//        // Create particle effect
//        fire = new ParticleEffect();
//        fire.load(Gdx.files.internal("data/sparks"), Gdx.files.internal("data"));
//        fire.getEmitters().first().setPosition(toPixelsX(-BEAM_LENGTH / 2f - WHITE_RADIUS), toPixelsY(0f));
//        fire.start();
    }

    @Override
    public void render(float delta) {
        //super.render(delta);

        switch (gameState) {

            case INSTRUCTIONS:
                displayLevelName();
                instructions(500);
                break;

            case RUNNING:
                drawTray(delta);
                // Is the game over?
                if (whiteNearEnough()) {
                    finishTimeSeconds = (int) ((System.currentTimeMillis() - startTimeMilliseconds) / 1000);
                    //stopRunningGame();
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
        font.draw(spriteBatch, "The elastic is now invisible ...", 200, yPos);


        spriteBatch.end();
    }

    void physics() {

        Vector2 g = getG();
        white.move(g);


        //Gdx.app.log("Beam01: linvelx", " " + white.box2dBody.getLinearVelocity().x);

        // Update the particle emitter
        //fire.update(delta);

        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        // Draw white ball destination particle effect
//        spriteBatch.begin();
//        fire.draw(spriteBatch);
//        spriteBatch.end();
//        if (fire.isComplete())
//            fire.reset();

        perspCam.position.set(0, -H, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

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

//        Vector2 whitePos = white.box2dBody.getPosition();
//        Vector2 whiteVel = white.box2dBody.getLinearVelocity();

        final float SPEED_THRESHOLD = 3f;
        final float DISTANCE_THRESHOLD = WHITE_RADIUS;

        float beamAngle = beam.box2dBody.getAngle();

//        Vector2 whiteGoalPos = new Vector2((float) Math.cos((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS),
//                (float) Math.sin((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS));

//        Vector2 whiteGoalVel = new Vector2(
//                (float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY,
//                (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY);

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

    boolean nearEnoughOld() {

        Vector2 whitePos = white.box2dBody.getPosition();
        Vector2 whiteVel = white.box2dBody.getLinearVelocity();

        final float SPEED_THRESHOLD = 3f;
        final float DISTANCE_THRESHOLD = WHITE_RADIUS;

        float beamAngle = beam.box2dBody.getAngle();

        Vector2 whiteGoalPos = new Vector2((float) Math.cos((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS),
                (float) Math.sin((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS));

        Vector2 whiteGoalVel = new Vector2(
                (float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY,
                (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY);

        float whiteDistanceFromGoal = whiteGoalPos.sub(whitePos).len();

        float whiteRelVelToGoal = whiteVel.sub(whiteGoalVel).len();

        boolean whiteRelativelyNear = whiteDistanceFromGoal < DISTANCE_THRESHOLD;

        boolean whiteRelativelySlow = whiteRelVelToGoal < SPEED_THRESHOLD;

        return whiteRelativelyNear && whiteRelativelySlow;
    }

}
