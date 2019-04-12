package com.purelazy.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Utils {
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static void pixel(float x, float y, Color colour) {
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(x, y, 20f);
        shapeRenderer.end();
    }

    public static void pixel(float x, float y, Color colour, float size) {
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(x, y, size);
        shapeRenderer.end();
    }

    public static void pixel(Vector2 pos, Color colour, float size) {
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, size);
        shapeRenderer.end();
    }

    public static void circle(Vector2 pos, Color colour, float size) {
        Vector2 pixelPos = worldToPixel(pos);
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(pixelPos.x, pixelPos.y, size);
        shapeRenderer.end();
    }


    public static void line(Vector2 pos, Vector2 pos2, Color colour, float size) {
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.rectLine(pos.x, pos.y, pos2.x, pos2.y, size);
        shapeRenderer.end();
    }

    public static void worldToPixelLine(Vector2 pos, Vector2 pos2, Color colour, float size) {
        Vector2 pixelPos, pixelPos2;
        pixelPos = worldToPixel(pos);
        pixelPos2 = worldToPixel(pos2);
        shapeRenderer.setColor(colour);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.rectLine(pixelPos.x, pixelPos.y, pixelPos2.x, pixelPos2.y, size);
        shapeRenderer.end();
    }

    public static Vector2 worldToPixel(Vector2 worldPos) {
        return new Vector2((worldPos.x * Tray.PPWU) + (Tray.wfloat / 2f), (worldPos.y * Tray.PPWU) + (Tray.hfloat / 2f));
    }


    protected static void nextScreen(String level, Start topLevel) {

        if (level.equals("SpiritLevel")) {
            topLevel.setScreen(new Ball01_SpiritLevel(topLevel));
            return;
        }

        if (level.equals("SpringLevel")) {
            topLevel.setScreen(new Ball01_SpringLevel(topLevel));
            return;
        }

        if (level.equals("BallPark")) {
            topLevel.setScreen(new Ball01_BallPark(topLevel));
            return;
        }

        if (level.equals("WhiteBall")) {
            topLevel.setScreen(new Beam01_WhiteBall(topLevel));
            return;
        }

        if (level.equals("WhiteBallNoElastic")) {
            topLevel.setScreen(new Beam01_WhiteBallNoElastic(topLevel));
            return;
        }

        if (level.equals("WhiteAndRocky")) {
            topLevel.setScreen(new Beam02_WhiteAndRocky(topLevel));
            return;
        }

        if (level.equals("WhiteAndRockySpin")) {
            topLevel.setScreen(new Beam02_WhiteAndRockySpin(topLevel));
            return;
        }

        if (level.equals("MeetGreen")) {
            topLevel.setScreen(new Beam03_MeetGreen(topLevel));
            return;
        }

        if (level.equals("MeetGreenSpin")) {
            topLevel.setScreen(new Beam03_MeetGreenSpin(topLevel));
            return;
        }

        if (level.equals("RedKillsWhite")) {
            topLevel.setScreen(new Beam06_RedKillsWhite(topLevel));
            return;
        }

        if (level.equals("BallPark4")) {
            topLevel.setScreen(new Ball01_BallPark4(topLevel));
            return;
        }

        if (level.equals("Maze16")) {
            topLevel.setScreen(new Maze01_Maze16(topLevel));
            return;
        }

        if (level.equals("MazeFresh")) {
            topLevel.setScreen(new Maze01_MazeFresh(topLevel));
            return;
        }

    }

    protected static void nextScreen(Screen screen, Start topLevel) {
        topLevel.setScreen(new Menu01_LevelChooser(topLevel));
    }

}
