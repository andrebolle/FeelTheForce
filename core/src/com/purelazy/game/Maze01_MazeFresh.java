package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Quaternion;
//import com.badlogic.gdx.math.Vector3;

public class Maze01_MazeFresh extends Tray {

    // The game objects
    private BallInSpace white;

    Vector2 whiteGoalPos = new Vector2(1.5f-8f, 1.5f-8f);

//    private Vector2[] walls = {
//            new Vector2(-7, -7), new Vector2(-6, -7),
//    };

    ArrayList<Vector2> walls = new ArrayList<Vector2>();
    ArrayList<Barrier> barriers = new ArrayList<Barrier>();

    private static Random random = new Random(System.currentTimeMillis());


    public Maze01_MazeFresh(Start game) {
        super(game);
        white = new BallInSpace(world, new Vector2(16 / 2.0f + 0.5f -8, 16 / 2.0f + 0.5f-8), 0.2f, Color.WHITE);
        init();
        generate();
        build();
        makeBarriers();
    }

    private void makeBarriers() {
        for (int index = 0; index < walls.size(); index += 2) {
            barriers.add(new Barrier(world, walls.get(index), walls.get(index+1)));
        }
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
        font.draw(spriteBatch, "Get the white ball to the red dot.", 200, yPos);
        spriteBatch.end();
    }

    void physics() {
        Vector2 g = getG();
        white.move(g);
        world.step(1 / 60f, 6, 2);
    }

    void draw() {

//        perspCam.position.set(0, -H, CAMERA_DISTANCE);
//        perspCam.lookAt(0, 0, 0);
//        perspCam.update();
//
//        Utils.line(worldToPixel(center), worldToPixel(white.box2dBody.getPosition()), Color.DARK_GRAY, 5f);


        Utils.circle(whiteGoalPos, Color.RED, 6f);

        modelBatch.begin(perspCam);
        modelBatch.render(white.modelInstance, environment);
        // debugRenderer.render(world, debugMatrix);
        modelBatch.end();
        Gdx.app.log("********", "MazeFresh.draw()");

        for (int index = 0; index < walls.size(); index += 2) {
            Utils.worldToPixelLine(walls.get(index), walls.get(index+1), Color.BLACK, 3);
        }

        //StdDraw.filledCircle(1.5, 1.5, 0.375);


    }

    boolean whiteNearEnough() {
            float whiteDistanceFromGoal = whiteGoalPos.cpy().sub(white.box2dBody.getPosition()).len();
            return (whiteDistanceFromGoal < 0.2f);
    }


    private int N = 16;

    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;



    private void init() {
        // initialize border cells as already visited
        visited = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++) {
            visited[x][0] = true;
            visited[x][N+1] = true;
        }
        for (int y = 0; y < N+2; y++) {
            visited[0][y] = true;
            visited[N+1][y] = true;
        }


        // initialze all walls as present
        north = new boolean[N+2][N+2];
        east  = new boolean[N+2][N+2];
        south = new boolean[N+2][N+2];
        west  = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++) {
            for (int y = 0; y < N+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }

    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
//                double r = StdRandom.uniform(4);
                int r = random.nextInt(4);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);
    }


    public void build() {
//        StdDraw.setPenColor(StdDraw.RED);
//        StdDraw.filledCircle(N/2.0 + 0.5, N/2.0 + 0.5, 0.375);
//        StdDraw.filledCircle(1.5, 1.5, 0.375);

//        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= N; x++) {
            for (int y = 1; y <= N; y++) {
                if (south[x][y]) {
//                    StdDraw.line(x, y, x + 1, y);
                    walls.add(new Vector2((x - N/2) , (y-N/2) )); walls.add(new Vector2((x+1-N/2), (y-N/2) ));;
                }
                if (north[x][y]) {
//                    StdDraw.line(x, y + 1, x + 1, y + 1);
                    walls.add(new Vector2((x - N/2), ((y-N/2)+1) )); walls.add(new Vector2(((x - N/2)+1), ((y-N/2)+1) ));
                }
                if (west[x][y])  {
//                    StdDraw.line(x, y, x, y + 1);
                    walls.add(new Vector2((x - N/2), (y-N/2) )); walls.add(new Vector2((x - N/2) , ((y-N/2)+1) ));
                }
                if (east[x][y])  {
//                    StdDraw.line(x + 1, y, x + 1, y + 1);
                    walls.add(new Vector2(((x - N/2)+1) ,  (y-N/2) )); walls.add(new Vector2(((x - N/2)+1) , ((y-N/2)+1) ));
                }
            }
        }
//        StdDraw.show(1000);
    }

}
