package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Beam06_RedKillsWhite extends Tray {

    private static final float BEAM_ANGULAR_VELOCITY = 0.0f;
    // The game objects
    private BallOnSpring white, red;
    private Beam beam;
    // Particle effect
    //private ParticleEffect redSparks;

    public Beam06_RedKillsWhite(Start game) {
        super(game);
        buildScene();
    }

    private void buildScene() {
        // Create beam
        beam = new Beam(world, BEAM_LENGTH, BEAM_WIDTH, BEAM_DEPTH, BEAM_ANGULAR_VELOCITY);

        // Create balls
        white = new BallOnSpring(world, new Vector2(0f, BEAM_LENGTH / 2f), WHITE_RADIUS, Color.WHITE);
        red = new BallOnSpring(world, new Vector2(-BEAM_LENGTH / 2f, 0f), RED_RADIUS, Color.RED);

        // Create particle effect
        //redSparks = new ParticleEffect();
        //redSparks.load(Gdx.files.internal("data/redsparks"), Gdx.files.internal("data"));
        //redSparks.start();

        setUpContactListener();
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
        font.draw(spriteBatch, "Don't touch the red ball or it's game over!", 200, yPos);
        spriteBatch.end();
    }

    void physics() {

        Vector2 g = getG();
        white.move(g);
        red.move(g);
        beam.rotate();

//        redSparks.getEmitters().first().setPosition(toPixelsX(red.box2dBody.getPosition().x),
//                toPixelsY(red.box2dBody.getPosition().y));
//        redSparks.update(delta);

        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        perspCam.position.set(0, -H, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

        modelBatch.begin(perspCam);
        modelBatch.render(beam.modelInstance, environment);
        modelBatch.render(white.modelInstance, environment);
        modelBatch.render(red.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        modelBatch.end();

        perspCam.position.set(0, 0, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

//        // Draw particle effect
//        spriteBatch.begin();
//        redSparks.draw(spriteBatch);
//        spriteBatch.end();
    }

    boolean nearEnough() {
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

    private void setUpContactListener() {

        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                if ((contact.getFixtureA().getBody() == red.box2dBody
                        && contact.getFixtureB().getBody() == white.box2dBody)
                        || (contact.getFixtureA().getBody() == white.box2dBody
                        && contact.getFixtureB().getBody() == red.box2dBody)) {
                    finishTimeSeconds = FINISH_TIME_IF_RED_HITS;
                    updateHighScore(finishTimeSeconds);
                    gameState = GameState.FINISHED;
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }
}