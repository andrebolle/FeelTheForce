package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Beam02_WhiteAndRocky extends Tray {

    // The game objects
    private BallOnSpring white;
    private Rock rock;
    private Beam beam;
    // Particle effect
    //private ParticleEffect fire;

    public Beam02_WhiteAndRocky(Start game) {
        super(game);
        buildScene();
    }

    private void buildScene() {
        // Create beam
        beam = new Beam(world, BEAM_LENGTH, BEAM_WIDTH, BEAM_DEPTH, BEAM_ANGULAR_VELOCITY);

        // Create balls
        white = new BallOnSpring(world, new Vector2(0f, BEAM_LENGTH / 2f), WHITE_RADIUS, Color.WHITE);

        Texture ballTexture = new Texture(Gdx.files.internal("data/stone.png"));
        rock = new Rock(world, new Vector2(0f, -BEAM_LENGTH / 2f), ROCK_RADIUS, ballTexture);

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
                if (nearEnough()) {
                    finishTimeSeconds = (int) ((System.currentTimeMillis() - startTimeMilliseconds) / 1000);
                    updateHighScore(finishTimeSeconds);
                    gameState = GameState.FINISHED;
                    break;
                }

                draw();
                physics(delta);
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
        font.draw(spriteBatch, "White ball, white EDGE. Rocky ball, rocky EDGE.", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "At the same time.", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "When the balls are in position,", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "Hold STILL. Hold FLAT and WAIT", 200, yPos);
        spriteBatch.end();
    }

    void physics(float delta) {

        Vector2 g = getG();
        white.move(g);
        //rock.move(g);
        rock.move(g);

//		rock.box2dBody.applyForceToCenter(-rock.box2dBody.getPosition().x * rock.springStiffness + g.y,
//				-rock.box2dBody.getPosition().y * rock.springStiffness - g.x, true);
//
//		// Rotations
//		rock.modelInstance.transform = new Matrix4(
//				new Vector3(rock.box2dBody.getPosition().x, rock.box2dBody.getPosition().y, 0),
//				new Quaternion(new Vector3(0f, 0f, 1f), rock.box2dBody.getAngle() * RADIANS_TO_DEGREES),
//				new Vector3(1f, 1f, 1f));

//		// Apply forces to box2D objects
//		white.box2dBody.applyForceToCenter(-white.box2dBody.getPosition().x * SPRING_STIFFNESS + g.y,
//				-white.box2dBody.getPosition().y * SPRING_STIFFNESS - g.x, true);
//
//		// Translations
//		white.modelInstance.transform
//				.setToTranslation(new Vector3(white.box2dBody.getPosition().x, white.box2dBody.getPosition().y, 0));

        // Not needed
        // Rotate the beam
//		beam.modelInstance.transform = new Matrix4(
//				new Vector3(beam.box2dBody.getPosition().x, beam.box2dBody.getPosition().y, 0),
//				new Quaternion(new Vector3(0f, 0f, 1f), beam.box2dBody.getAngle() * RADIANS_TO_DEGREES),
//				new Vector3(1f, 1f, 1f));

        // Update the particle emitter
//        fire.update(delta);

        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        // Draw white ball destination first
//        spriteBatch.begin();
//        fire.draw(spriteBatch);
//        spriteBatch.end();
//        if (fire.isComplete())
//            fire.reset();

        perspCam.position.set(0, -H, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

        Utils.line(worldToPixel(center), worldToPixel(white.box2dBody.getPosition()), Color.DARK_GRAY, 5f);
        Utils.line(worldToPixel(center), worldToPixel(rock.box2dBody.getPosition()), Color.DARK_GRAY, 5f);


        modelBatch.begin(perspCam);
        modelBatch.render(beam.modelInstance, environment);
        modelBatch.render(white.modelInstance, environment);
        modelBatch.render(rock.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        modelBatch.end();

        perspCam.position.set(0, 0, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.update();

    }

    boolean nearEnough() {

        Vector2 whitePos = white.box2dBody.getPosition();
        Vector2 rockPos = rock.box2dBody.getPosition();
        Vector2 whiteVel = white.box2dBody.getLinearVelocity();
        Vector2 rockVel = rock.box2dBody.getLinearVelocity();

        final float SPEED_THRESHOLD = 3f;
        final float DISTANCE_THRESHOLD = WHITE_RADIUS;

        float beamAngle = beam.box2dBody.getAngle();

        Vector2 whiteGoalPos = new Vector2((float) Math.cos((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS),
                (float) Math.sin((double) beamAngle) * (-BEAM_LENGTH / 2f - WHITE_RADIUS));
        Vector2 rockGoalPos = new Vector2((float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + ROCK_RADIUS),
                (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + ROCK_RADIUS));

        Vector2 whiteGoalVel = new Vector2(
                (float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY,
                (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + WHITE_RADIUS) * BEAM_ANGULAR_VELOCITY);
        Vector2 rockGoalVel = new Vector2(
                (float) Math.cos((double) beamAngle) * (BEAM_LENGTH / 2f + ROCK_RADIUS) * BEAM_ANGULAR_VELOCITY,
                (float) Math.sin((double) beamAngle) * (BEAM_LENGTH / 2f + ROCK_RADIUS) * BEAM_ANGULAR_VELOCITY);

        float whiteDistanceFromGoal = whiteGoalPos.sub(whitePos).len();
        float rockDistanceFromGoal = rockGoalPos.sub(rockPos).len();

        float whiteRelVelToGoal = whiteVel.sub(whiteGoalVel).len();
        float rockRelVelToGoal = rockVel.sub(rockGoalVel).len();

        boolean whiteRelativelyNear = whiteDistanceFromGoal < DISTANCE_THRESHOLD;
        boolean rockRelativelyNear = rockDistanceFromGoal < DISTANCE_THRESHOLD;

        boolean whiteRelativelySlow = whiteRelVelToGoal < SPEED_THRESHOLD;
        boolean rockRelativelySlow = rockRelVelToGoal < SPEED_THRESHOLD;

        boolean gameOver = whiteRelativelyNear && rockRelativelyNear && whiteRelativelySlow && rockRelativelySlow;

        // Gdx.app.debug(
        // "Closeness",
        // "" + whiteDistanceFromGoal + "\t\t" + rockDistanceFromGoal);
        // Gdx.app.debug(
        // "Slowness",
        // "" + whiteRelVelToGoal + "\t\t" + rockRelVelToGoal);

        return gameOver;
    }
}
