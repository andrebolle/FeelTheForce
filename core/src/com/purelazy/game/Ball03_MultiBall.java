package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Ball03_MultiBall extends Tray {

    ArrayList<Ball> balls = new ArrayList<Ball>();
    // The game objects

    public Ball03_MultiBall(Start game) {
        super(game);
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.WHITE));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.RED));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.BLUE));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.WHITE));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.RED));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.BLUE));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.WHITE));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.RED));
        balls.add(new Ball(world, new Vector2(0f, 0f), 1f, Color.BLUE));
    }

    @Override
    public void render(float delta) {
        //super.render(delta);

        switch (gameState) {

            case INSTRUCTIONS:
                displayLevelName(); // New code
                instructions(500);
                break;

            case RUNNING:
                drawTray(delta); // new code

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
                displayFinalScore(delta); // new code
                draw();
                break;

            case NEXT:
                Utils.nextScreen(this, topLevel);
                break;
        }
    }

    void instructions(int yPos) {
        spriteBatch.begin();
        font.draw(spriteBatch, "Tilt the tray to move the ball.", 200, yPos);
        spriteBatch.end();
    }

    void physics(float delta) {
        Vector2 g = getG();
        for (Ball b : balls) {
            b.move(g);
        }

        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        modelBatch.begin(perspCam);
        //modelBatch.render(white.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        // rubbish shapeRenderer.setProjectionMatrix(orthoCam.combined);

        Utils.pixel(worldToPixel(new Vector2(0, 0)),Color.RED,20);
        Utils.pixel(worldToPixel(new Vector2(0, H/2)),Color.BLUE,20);
        Utils.pixel(worldToPixel(new Vector2(W/2, H/2)),Color.GREEN,20);

        for (Ball b : balls) {
            modelBatch.render(b.modelInstance, environment);
        }
        modelBatch.end();
    }

    boolean nearEnough() {
        return false;
    }

    @Override
    protected void changeState() {
        switch (gameState) {
            case INSTRUCTIONS:
                gameState = GameState.RUNNING;
                break;

            case RUNNING:
                gameState = GameState.NEXT;
                break;

            case FINISHED:
                //gameState = GameState.NEXT;
                break;

            case NEXT:
                // I'll be dead by the time I get used
                break;
        }
    }


}
