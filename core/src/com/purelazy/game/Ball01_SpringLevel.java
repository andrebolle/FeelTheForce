package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Ball01_SpringLevel extends Tray {

    // The game objects
    private BallOnSpring white;

    public Ball01_SpringLevel(Start game) {
        super(game);
        // Start running
        gameState = GameState.RUNNING;
        white = new BallOnSpring(world, new Vector2(0f, 0f), 1f, Color.WHITE);
    }

    @Override
    public void render(float delta) {
        //super.render(delta);

        switch (gameState) {

            case INSTRUCTIONS:
                //levelSplash2(delta);
                instructions(500);
                // Could throw a should not be here exception
                break;

            case RUNNING:
                drawTray(delta);
                instructions(500);
                // Is the game over?
                if (nearEnough()) {
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
        font.draw(spriteBatch, "A ball on elastic.", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "Can you make it go in circles?", 200, yPos);
        font.draw(spriteBatch, "Tap the screen to continue", 400, 100);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        Utils.line(worldToPixel(center), worldToPixel(white.box2dBody.getPosition()), Color.PINK, 3f);
        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
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
