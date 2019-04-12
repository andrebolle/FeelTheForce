package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Collections;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Menu01_LevelChooser extends Tray {

    // The game objects
    private BallParkMenu white;
    private Vector2[] menuPositions = {
            new Vector2(-16, 4), // 1
            new Vector2(-12, 4),
            new Vector2( -8, 4),
            new Vector2( -4, 4),
            new Vector2(  0, 4),
            new Vector2(  4, 4),
            new Vector2(  8, 4),
            new Vector2( 12, 4),
            new Vector2( 16, 4), // 9
            new Vector2(-16, 0), // 10
            new Vector2(-12, 0), // 11
            new Vector2( -8, 0), // 12
            new Vector2( -4, 0), // 12
    };
    private String[] menuText = {
            Ball01_SpiritLevel.class.getSimpleName().substring(7), // 1
            Ball01_SpringLevel.class.getSimpleName().substring(7),
            Ball01_BallPark.class.getSimpleName().substring(7),
            Beam01_WhiteBall.class.getSimpleName().substring(7),
            Beam01_WhiteBallNoElastic.class.getSimpleName().substring(7),
            Beam02_WhiteAndRocky.class.getSimpleName().substring(7),
            Beam02_WhiteAndRockySpin.class.getSimpleName().substring(7),
            Beam03_MeetGreen.class.getSimpleName().substring(7),
            Beam03_MeetGreenSpin.class.getSimpleName().substring(7), // 9
            Beam06_RedKillsWhite.class.getSimpleName().substring(7), // 10
            Ball01_BallPark4.class.getSimpleName().substring(7), // 11
            Maze01_Maze16.class.getSimpleName().substring(7), // 12
            Maze01_MazeFresh.class.getSimpleName().substring(7), // 12
    };

    final float SPEED_THRESHOLD = 3f;
    final float DISTANCE_THRESHOLD = 1f;

    private int totalScore;

    public Menu01_LevelChooser(Start game) {
        super(game);
        white = new BallParkMenu(world, new Vector2(-5f, -5f), 1f, Color.RED, menuPositions, menuText);
        totalScore = getOverallScore();
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

//                if (nearEnough()) {
//                    finishTimeSeconds = (int) ((System.currentTimeMillis() - startTimeMilliseconds) / 1000);
//                    updateHighScore(finishTimeSeconds);
//                    gameState = GameState.FINISHED;
//                    break;
//                }

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
        font.draw(spriteBatch, "Choose a Level!", 200, yPos);
        yPos -= 60;
        font.draw(spriteBatch, "Time to beat " + getOverallScore(), 200, yPos);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        String levelChosen = white.move(g, this);
        if (levelChosen != null) Utils.nextScreen(levelChosen, topLevel);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {
        // Draw hole
        for (int index = 0; index < menuPositions.length; index++) {
            Utils.pixel(worldToPixel(menuPositions[index]), Color.BLACK, 10f);
        }

        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        modelBatch.end();
    }


//    private boolean nearEnough() {
//        float whiteDistanceFromGoal = hole.cpy().sub(white.box2dBody.getPosition()).len();
//        float whiteRelVelToGoal = white.box2dBody.getLinearVelocity().len();
//
//        boolean whiteRelativelyNear = whiteDistanceFromGoal < DISTANCE_THRESHOLD;
//
//        boolean whiteRelativelySlow = whiteRelVelToGoal < SPEED_THRESHOLD;
//
//        return whiteRelativelyNear && whiteRelativelySlow;
//    }

    // For the menu, override setupBackground so that only a WHITE tray is used
    @Override
protected void setupBackground() {
    // Get the background
    FileHandle trayFile = Gdx.files.internal("data/tray_background.jpg");

    //Texture backgroundTexture = new Texture("data/tray_background.jpg");
    Texture backgroundTexture = new Texture(trayFile, Pixmap.Format.RGB565, false);

    // Get its dimentsions for later
    iw = backgroundTexture.getWidth();
    ih = backgroundTexture.getHeight();

    // Create a texture
    TextureRegion region = new TextureRegion(backgroundTexture);
    Sprite backgroundSprite = new Sprite(region);
    backgroundSprite.setSize(w, h);

    FileHandle trayOnlyFile = Gdx.files.internal("data/tray_only.png");

    //Texture trayOnlyTexture = new Texture("data/tray_only.png");
    Texture trayOnlyTexture = new Texture(trayOnlyFile, Pixmap.Format.RGBA4444, false);

    TextureRegion region2 = new TextureRegion(trayOnlyTexture);
    Sprite trayOnlySprite = new Sprite(region2);
    trayOnlySprite.setSize(wfloat, hfloat);

    // Pick a colour for the tray
    Collections.shuffle(colors);
    Color c = Color.WHITE;
    trayOnlySprite.setColor(c);

    // Render the background and the tray to compositeBackground
    spriteBatch.begin();
    backgroundSprite.draw(spriteBatch);
    trayOnlySprite.draw(spriteBatch);
    spriteBatch.end();

    compositeBackground = ScreenUtils.getFrameBufferTexture(0, 0, w, h);
}

}
