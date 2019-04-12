package com.purelazy.game;


// Shift-F10 = Run
// Shift-F6  = Rename
// Cntl-Alt-L = Reformat Code
// F2         = Next Highlighted Error
// Cntl-F3    = Find Word At Caret
// F3         = Find Next

/*

    If you use a Camera (which you should) changing the coordinate system is pretty simple:

    camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    If you use TextureRegions and/or a TextureAtlas, all you need to do in addition to that is call
    region.flip(false, true).

    The reasons we use y-up by default (which you can easily change as illustrated above) are
    as follows:

        your simulation code will most likely use a standard euclidian coordinate system with y-up
        if you go 3D you have y-up
        The default coordinate system is a right handed one in OpenGL, with y-up. You can of
        course easily change that with some matrix magic.

    The only two places in libgdx where we use y-down are:

        Pixmap coordinates (top upper left origin, y-down)
        Touch event coordinates which are given in window coordinates (top upper left origin, y-down)

    Again, you can easily change the used coordinate system to whatever you want using either
    Camera or a tiny bit of matrix math.

*/


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


// This is the base class for all Tray screens
// It provides: State, Screen switching, Screen name, Tray image, Barriers,
// Persistent Best Scores, Lighting, Camera, Box2D world,
// Model & Sprite batching, Font

// When adding a new level, remember to:
// 1. Update getOverall score in this file.
// 2. Update nextScreen in Utils.
// 3. Update menuText & menuPosition in LevelChooser

abstract public class Tray implements Screen, InputProcessor {
    //
    protected Start topLevel;
    protected String screenName;

    // Screen Pixel/World Unit widths & heights
    protected int w, h;               // Screen width height
    protected int iw, ih;             // Background image width & height
    protected static float wfloat, hfloat;               // Screen width height as floats
    protected static final float H = 32.0f; // Screen height in world units
    protected static float ar;              // aspect ratio
    protected static float W;               // Width in world units, H * aspect ratio
    protected static float PPWU;            // pixels per world unit - hfloat/H - assumes square pixels


    protected static final float CAMERA_DISTANCE = 100f;

    // Position of score
    protected static final float SCORE_POS_X = 110f;
    protected static final float SCORE_POS_Y = 150f;

    protected static final float BEAM_LENGTH = 15f;
    protected static final float BEAM_WIDTH = BEAM_LENGTH / 5f;
    protected static final float BEAM_DEPTH = BEAM_LENGTH / 4f;
    protected static final float WHITE_RADIUS = 1f;
    protected static final float ROCK_RADIUS = 2f;
    protected static final float GREEN_RADIUS = 1.5f;
    protected static final float RED_RADIUS = 1.2f;

    protected static final int FINISH_TIME_IF_RED_HITS = 180;

    protected static final float BEAM_ANGULAR_VELOCITY = 0.0f;


    // Centre of screen in pixels (lower-left is (0,0)
    protected static Vector2 screenCentre;
    // Multiply by these to convert between radians and degrees
    protected final float RADIANS_TO_DEGREES = 180f / (float) Math.PI;
    protected GameState gameState = GameState.INSTRUCTIONS;

    protected String playerAndScreenName;
    protected Preferences scoresDatabase = Gdx.app.getPreferences("scoresDatabase");
    // Lights
    protected Environment environment;
    //protected GameState nextGameState = GameState.RUNNING;
    // Camera
    protected PerspectiveCamera perspCam;
    //protected OrthographicCamera orthoCam;
    // Particle effect
    protected ParticleEffect starField;
    // 3D Model batch drawing
    protected ModelBatch modelBatch = new ModelBatch();
    // World manages memory, objects, and physics simulation.
    protected World world = new World(new Vector2(0, 0), true);
    // Sprite batch drawing
    protected SpriteBatch spriteBatch = new SpriteBatch();
    // Font used of score (seconds elapsed)
//    protected BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"),
//            Gdx.files.internal("fonts/white_font.png"), false);

    // New improved TrueType font.
    protected FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/amiga.ttf"));
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter = getFontParameter();
    protected BitmapFont font = generator.generateFont(parameter);

    private static FreeTypeFontGenerator.FreeTypeFontParameter getFontParameter() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        return parameter;
    }

    // Time
    protected long startTimeMilliseconds = System.currentTimeMillis();
    protected int finishTimeSeconds;

    private Barrier ab, bc, cd, de, ef, fa;

    // A list of colours
    List<Color> colors = Arrays.asList(Color.CYAN,
            Color.PINK, Color.ORANGE, Color.MAGENTA,
            Color.RED, Color.BLUE, Color.BLACK,
            Color.DARK_GRAY, Color.WHITE, Color.MAROON,
            Color.GREEN, Color.TEAL, Color.YELLOW
    );

    private Matrix4 debugMatrix;
    //Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    //private Sprite backgroundSprite;
    TextureRegion compositeBackground;

    private Vector2 accelerometer = new Vector2(0f, 0f);

    protected final Vector2 center = new Vector2(0f, 0f);

    private final int DEFAULT_SCORE = 180;



    public Tray(Start game) {
        topLevel = game;
        screenName = this.getClass().getSimpleName().substring(7);
        playerAndScreenName = topLevel.playerName + " " + screenName;

        w = Gdx.graphics.getWidth();    // width (int) in pixels
        h = Gdx.graphics.getHeight();   // height (int) in pixels
        wfloat = (float)w;
        hfloat = (float)h;
        ar = wfloat/hfloat;             // aspect ration
        W = H * ar;           // width in world units
        PPWU = hfloat/H;                     // pixels per world unit

        screenCentre = new Vector2(wfloat / 2f, hfloat / 2f);

        // Setup up the infrastructure of the game
        setupBackground();
        setupLights();
        setupCamera();
        //setupOrthoCamera();
        setupStarField();
        setupBarriers();

        // Sets the InputProcessor that will receive all touch and key input events.
        // It will be called before the ApplicationListener#render() method each frame.
        Gdx.input.setInputProcessor(this);

        // Box2D debugging
        debugMatrix = new Matrix4(perspCam.combined);
    }

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
        Color c = colors.get(0);
        trayOnlySprite.setColor(c);

        // Render the background and the tray to compositeBackground
        spriteBatch.begin();
        backgroundSprite.draw(spriteBatch);
        trayOnlySprite.draw(spriteBatch);
        spriteBatch.end();

        compositeBackground = ScreenUtils.getFrameBufferTexture(0, 0, w, h);
    }

    private void setupStarField() {
        // Create particle effect
        starField = new ParticleEffect();
        starField.load(Gdx.files.internal("data/stars"), Gdx.files.internal("data"));
        starField.getEmitters().first().setPosition(screenCentre.x, screenCentre.y);
        starField.start();
    }

    private void setupLights() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -1f, -1f));
    }

    // Camera
    //
    //.|\
    //.|.\
    //.|..\
    //.|...\
    //O|....\.H
    //.|.....\
    // |......\
    // |.......\
    // |......a.\
    // |_________\
    //
    // Consider a right angled triangle
    //
    // O = H/2
    // A = CAMERA DISTANCE from origin.
    // a = angle opposite O
    // tan a = O/A
    // a = arctan(O/A) = Math.atan(SCREEN_HEIGHT/2/CAMERA_DISTANCE)
    // fieldOfView = a * 2 (in radians)

    private void setupCamera() {
        // Set field of view to see all the screen height for a given camera
        // distance

        float fieldOfView = (float) Math.atan(H / 2f / CAMERA_DISTANCE) * 2f
                * RADIANS_TO_DEGREES;

        // perspCam = new PerspectiveCamera(67 /* FOV */, w,

        perspCam = new PerspectiveCamera(fieldOfView /* FOV */, w, h);
        perspCam.position.set(0, 0, CAMERA_DISTANCE);
        perspCam.lookAt(0, 0, 0);
        perspCam.near = CAMERA_DISTANCE - 10f;
        perspCam.far = CAMERA_DISTANCE + 10f;
        perspCam.update();

    }

//    private void setupOrthoCamera() {
//        orthoCam= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        orthoCam.viewportWidth = w;
//        orthoCam.viewportHeight = h;
//        orthoCam.position.set(orthoCam.viewportWidth * .5f,
//                orthoCam.viewportHeight * .5f, 0f);
//        orthoCam.update();
//        //camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//    }

    private void setupBarriers() {
        // Points for defining the lines of the edges of the tray

        Vector2 A = new Vector2(238, 52); // GIMP
        Vector2 B = new Vector2(1126, 54); // GIMP
        Vector2 C = new Vector2(1185, 358);
        Vector2 D = new Vector2(1107, 722); // GIMP
        Vector2 E = new Vector2(264, 726); // GIMP
        Vector2 F = new Vector2(190, 366);

        ab = new Barrier(world, imageToWorld(A), imageToWorld(B));
        bc = new Barrier(world, imageToWorld(B), imageToWorld(C));
        cd = new Barrier(world, imageToWorld(C), imageToWorld(D));
        de = new Barrier(world, imageToWorld(D), imageToWorld(E));
        ef = new Barrier(world, imageToWorld(E), imageToWorld(F));
        fa = new Barrier(world, imageToWorld(F), imageToWorld(A));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //debugMessage("TouchDown: " + button, 1);
        changeState();
        return true;
    }

    protected void changeState() {
        switch (gameState) {
            case INSTRUCTIONS:
                gameState = GameState.RUNNING;
                break;

            case RUNNING:
                //gameState = GameState.FINISHED;
                //gameState = GameState.NEXT; // andre
                break;

            case FINISHED:
                gameState = GameState.NEXT;
                break;

            case NEXT:
                // I'll be dead by the time I get used
                break;
        }
    }

    protected void displayLevelName() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Title
        spriteBatch.begin();
        String name = this.getClass().getSimpleName();
        // Don't print the BeamXX_ part. Start at 7!
        font.draw(spriteBatch, name.substring(7) /* 7 = start */, 200, 600);
        font.draw(spriteBatch, "Tap the screen to continue", 400, 100);
        spriteBatch.end();
    }

    protected void drawTray(float delta) {
        // No need to clear framebuffer because of the background image
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        // Draw background, tray, score
        spriteBatch.begin();
        spriteBatch.draw(compositeBackground, 0, 0);
//        font.draw(spriteBatch, "" + (int) (System.currentTimeMillis() - startTimeMilliseconds) / 1000,
//                SCORE_POS_X, SCORE_POS_Y);
        spriteBatch.end();

        drawStarField(delta);
    }

    protected void displayFinalScore(float delta) {
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        // Draw background, stars, objects, score
        spriteBatch.begin();
        //backgroundSprite.draw(spriteBatch);
        spriteBatch.draw(compositeBackground, 0, 0);

        spriteBatch.end();

        drawStarField(delta);

        spriteBatch.begin();
        //font.draw(spriteBatch, "Time taken: " + (int) finishTimeSeconds, SCORE_POS_X, SCORE_POS_Y);
        font.draw(spriteBatch, "Time: " + finishTimeSeconds, SCORE_POS_X, SCORE_POS_Y);
        font.draw(spriteBatch, "Best: " + getScore(), SCORE_POS_X, SCORE_POS_Y - 60f);
        font.draw(spriteBatch, "Tap the screen to continue", 400, 100);
        spriteBatch.end();
    }

//    protected void debugMessage(String message, int pos) {
//        spriteBatch.begin();
//        font.draw(spriteBatch, "" + pos + " " + message, 400, 200 + 60 * pos);
//        spriteBatch.end();
//    }

    protected void drawStarField(float delta) {
        // Draw particle effect
        spriteBatch.begin();
        starField.draw(spriteBatch);
        spriteBatch.end();
        if (starField.isComplete())
            starField.reset();
        starField.update(delta);
    }

    // Update best score if the latest time is better (less) than
    // the previous best score
    protected void updateHighScore(int latestScore) {
        int oldScore = getScore();
        // If there's no old score, set best score to current score
//        if (oldScore == DEFAULT_SCORE) {
//            setScore(latestScore);
//            // Update best score if latest score is less than the old score
//        }
//        else
        if (latestScore < oldScore) {
            setScore(latestScore);
        }
    }

    private int getScore() {
        return scoresDatabase.getInteger(playerAndScreenName, DEFAULT_SCORE);
    }

    protected int getOverallScore() {
        String name = topLevel.playerName;
        int total = 0;
        total += scoresDatabase.getInteger(name + " " + "BallPark", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "WhiteBall", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "WhiteBallNoElastic", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "WhiteAndRocky", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "WhiteAndRockySpin", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "MeetGreen", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "MeetGreenSpin", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "RedKillsWhite", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "BallPark4", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "Maze16", DEFAULT_SCORE);
        total += scoresDatabase.getInteger(name + " " + "MazeFresh", DEFAULT_SCORE);
        return total;
    }

    private void setScore(int score) {
        scoresDatabase.putInteger(playerAndScreenName, score);
        scoresDatabase.flush();
    }

    private final static float G_BOOST_FACTOR = 7f;
    protected Vector2 getG() {
        accelerometer.x = Gdx.input.getAccelerometerX() * G_BOOST_FACTOR;
        accelerometer.y = Gdx.input.getAccelerometerY() * G_BOOST_FACTOR;
        return accelerometer;
    }

    protected Vector2 imageToWorld(Vector2 pos) {
        float x = (pos.x / (float)iw * wfloat);
        float y = (pos.y / (float)ih * hfloat);
        Vector3 p = perspCam.unproject(new Vector3(x, y, 0), 0, 0, wfloat, hfloat);
        return new Vector2(p.x, p.y);
    }

    protected Vector2 worldToPixel(Vector2 worldPos) {
        return new Vector2((worldPos.x * PPWU) + (wfloat / 2f), (worldPos.y * PPWU) + (hfloat / 2f));
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        w = width;
        h = height;
    }

    @Override
    public void pause() {
    /*
     * On Android this method is called when the Home button
	 * is pressed or an incoming call is received. On desktop
	 * this is called just before dispose() when exiting the application.
	 * A good place to save the game state.
	 */
    }

    @Override
    public void resume() {
        // This method is only called on Android, when the application resumes from a paused state.
    }

    @Override
    public void hide() {
    }

//    @Override
//    public void dispose() {
//        //super.dispose();
//    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

	@Override
	public void dispose() {
		starField.dispose();
		modelBatch.dispose();
		world.dispose();
		spriteBatch.dispose();
		font.dispose();
	}

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    protected enum GameState {
        INSTRUCTIONS, RUNNING, FINISHED, NEXT
    }
}

