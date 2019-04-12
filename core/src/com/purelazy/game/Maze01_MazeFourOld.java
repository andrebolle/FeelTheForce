package com.purelazy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Maze01_MazeFourOld extends Tray {

    // The game objects
    private Ball white;

    private Vector2[] walls = {
            new Vector2(1, 1), new Vector2(2, 1),
            new Vector2(1, 2), new Vector2(2, 2),
            new Vector2(1, 1), new Vector2(1, 2),
            new Vector2(1, 2), new Vector2(2, 2),
            new Vector2(1, 2), new Vector2(1, 3),
            new Vector2(2, 2), new Vector2(2, 3),
            new Vector2(1, 3), new Vector2(1, 4),
            new Vector2(2, 3), new Vector2(2, 4),
            new Vector2(1, 4), new Vector2(1, 5),
            new Vector2(2, 4), new Vector2(2, 5),
            new Vector2(1, 5), new Vector2(1, 6),
            new Vector2(1, 6), new Vector2(1, 7),
            new Vector2(1, 7), new Vector2(1, 8),
            new Vector2(2, 7), new Vector2(2, 8),
            new Vector2(1, 9), new Vector2(2, 9),
            new Vector2(1, 8), new Vector2(1, 9),
            new Vector2(2, 1), new Vector2(3, 1),
            new Vector2(3, 1), new Vector2(3, 2),
            new Vector2(2, 3), new Vector2(3, 3),
            new Vector2(2, 2), new Vector2(2, 3),
            new Vector2(2, 3), new Vector2(3, 3),
            new Vector2(2, 3), new Vector2(2, 4),
            new Vector2(2, 5), new Vector2(3, 5),
            new Vector2(2, 4), new Vector2(2, 5),
            new Vector2(2, 5), new Vector2(3, 5),
            new Vector2(2, 6), new Vector2(3, 6),
            new Vector2(3, 5), new Vector2(3, 6),
            new Vector2(2, 6), new Vector2(3, 6),
            new Vector2(3, 6), new Vector2(3, 7),
            new Vector2(2, 8), new Vector2(3, 8),
            new Vector2(2, 7), new Vector2(2, 8),
            new Vector2(2, 8), new Vector2(3, 8),
            new Vector2(2, 9), new Vector2(3, 9),
            new Vector2(3, 8), new Vector2(3, 9),
            new Vector2(3, 1), new Vector2(4, 1),
            new Vector2(3, 2), new Vector2(4, 2),
            new Vector2(3, 1), new Vector2(3, 2),
            new Vector2(3, 2), new Vector2(4, 2),
            new Vector2(3, 3), new Vector2(4, 3),
            new Vector2(3, 3), new Vector2(4, 3),
            new Vector2(3, 4), new Vector2(4, 4),
            new Vector2(3, 4), new Vector2(4, 4),
            new Vector2(4, 4), new Vector2(4, 5),
            new Vector2(3, 5), new Vector2(3, 6),
            new Vector2(4, 5), new Vector2(4, 6),
            new Vector2(3, 7), new Vector2(4, 7),
            new Vector2(3, 6), new Vector2(3, 7),
            new Vector2(3, 7), new Vector2(4, 7),
            new Vector2(3, 9), new Vector2(4, 9),
            new Vector2(3, 8), new Vector2(3, 9),
            new Vector2(4, 1), new Vector2(5, 1),
            new Vector2(4, 3), new Vector2(5, 3),
            new Vector2(5, 2), new Vector2(5, 3),
            new Vector2(4, 3), new Vector2(5, 3),
            new Vector2(4, 4), new Vector2(5, 4),
            new Vector2(4, 4), new Vector2(5, 4),
            new Vector2(4, 4), new Vector2(4, 5),
            new Vector2(5, 4), new Vector2(5, 5),
            new Vector2(4, 5), new Vector2(4, 6),
            new Vector2(4, 7), new Vector2(5, 7),
            new Vector2(5, 6), new Vector2(5, 7),
            new Vector2(4, 7), new Vector2(5, 7),
            new Vector2(4, 8), new Vector2(5, 8),
            new Vector2(5, 7), new Vector2(5, 8),
            new Vector2(4, 8), new Vector2(5, 8),
            new Vector2(4, 9), new Vector2(5, 9),
            new Vector2(5, 1), new Vector2(6, 1),
            new Vector2(6, 1), new Vector2(6, 2),
            new Vector2(5, 3), new Vector2(6, 3),
            new Vector2(5, 2), new Vector2(5, 3),
            new Vector2(5, 3), new Vector2(6, 3),
            new Vector2(6, 3), new Vector2(6, 4),
            new Vector2(5, 5), new Vector2(6, 5),
            new Vector2(5, 4), new Vector2(5, 5),
            new Vector2(5, 5), new Vector2(6, 5),
            new Vector2(5, 6), new Vector2(6, 6),
            new Vector2(5, 6), new Vector2(6, 6),
            new Vector2(5, 6), new Vector2(5, 7),
            new Vector2(6, 6), new Vector2(6, 7),
            new Vector2(5, 7), new Vector2(5, 8),
            new Vector2(5, 9), new Vector2(6, 9),
            new Vector2(6, 8), new Vector2(6, 9),
            new Vector2(6, 1), new Vector2(7, 1),
            new Vector2(6, 2), new Vector2(7, 2),
            new Vector2(6, 1), new Vector2(6, 2),
            new Vector2(6, 2), new Vector2(7, 2),
            new Vector2(7, 2), new Vector2(7, 3),
            new Vector2(6, 3), new Vector2(6, 4),
            new Vector2(7, 3), new Vector2(7, 4),
            new Vector2(6, 5), new Vector2(7, 5),
            new Vector2(7, 4), new Vector2(7, 5),
            new Vector2(6, 5), new Vector2(7, 5),
            new Vector2(7, 5), new Vector2(7, 6),
            new Vector2(6, 7), new Vector2(7, 7),
            new Vector2(6, 6), new Vector2(6, 7),
            new Vector2(6, 7), new Vector2(7, 7),
            new Vector2(7, 7), new Vector2(7, 8),
            new Vector2(6, 9), new Vector2(7, 9),
            new Vector2(6, 8), new Vector2(6, 9),
            new Vector2(7, 1), new Vector2(8, 1),
            new Vector2(7, 2), new Vector2(8, 2),
            new Vector2(7, 2), new Vector2(8, 2),
            new Vector2(7, 2), new Vector2(7, 3),
            new Vector2(7, 3), new Vector2(7, 4),
            new Vector2(8, 3), new Vector2(8, 4),
            new Vector2(7, 4), new Vector2(7, 5),
            new Vector2(7, 6), new Vector2(8, 6),
            new Vector2(7, 5), new Vector2(7, 6),
            new Vector2(8, 5), new Vector2(8, 6),
            new Vector2(7, 6), new Vector2(8, 6),
            new Vector2(8, 6), new Vector2(8, 7),
            new Vector2(7, 8), new Vector2(8, 8),
            new Vector2(7, 7), new Vector2(7, 8),
            new Vector2(7, 8), new Vector2(8, 8),
            new Vector2(7, 9), new Vector2(8, 9),
            new Vector2(8, 1), new Vector2(9, 1),
            new Vector2(9, 1), new Vector2(9, 2),
            new Vector2(9, 2), new Vector2(9, 3),
            new Vector2(8, 4), new Vector2(9, 4),
            new Vector2(8, 3), new Vector2(8, 4),
            new Vector2(9, 3), new Vector2(9, 4),
            new Vector2(8, 4), new Vector2(9, 4),
            new Vector2(9, 4), new Vector2(9, 5),
            new Vector2(8, 5), new Vector2(8, 6),
            new Vector2(9, 5), new Vector2(9, 6),
            new Vector2(8, 6), new Vector2(8, 7),
            new Vector2(9, 6), new Vector2(9, 7),
            new Vector2(9, 7), new Vector2(9, 8),
            new Vector2(8, 9), new Vector2(9, 9),
            new Vector2(9, 8), new Vector2(9, 9),

    };

    ArrayList<Barrier> barriers = new ArrayList<Barrier>();


    public Maze01_MazeFourOld(Start game) {
        super(game);
        // Start running
        gameState = GameState.RUNNING;
        white = new Ball(world, new Vector2(8 / 2.0f + 0.5f, 8 / 2.0f + 0.5f), 0.2f, Color.WHITE);
        generateWalls();
    }

    private void generateWalls() {
        for (int index = 0; index < walls.length; index += 2) {
            barriers.add(new Barrier(world, walls[index], walls[index+1]));
        }
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
        font.draw(spriteBatch, "Tilt the tray to move the ball.", 200, yPos);
        font.draw(spriteBatch, "Tap the screen to continue", 400, 100);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        modelBatch.end();

        //StdDraw.filledCircle(N / 2.0 + 0.5, N / 2.0 + 0.5, 0.375);
        //StdDraw.filledCircle(1.5, 1.5, 0.375);

        for (int index = 0; index < walls.length; index += 2) {
            Utils.worldToPixelLine(walls[index], walls[index+1], Color.BLACK, 3);
        }


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
