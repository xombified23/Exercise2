package com.xombified23.connect4gdx.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    // Constants
    private final int NUMXSQUARE = 7;
    private final int NUMYSQUARE = 6;
    private final int SQUARESIZE = 100;
    private float groundCoord = 0;

    private final Texture dotTexture;
    private final Stage stage;
    private final ProjectApplication game;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private MapActor[][] mapActorList;
    private Rectangle dot;
    private boolean isDropping = false;
    private Array<Rectangle> dotsArray;

    public GameScreen(final ProjectApplication game) {
        this.game = game;
        stage = new Stage();
        dotTexture = new Texture(Gdx.files.internal("yellow.png"));
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        createGrid(NUMXSQUARE, NUMYSQUARE);

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
        stage.act(delta);
        stage.draw();

        MapActor currMapActor;
        if (Gdx.input.isTouched()) {
            Vector2 stageCoord;
            stageCoord = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if ((stage.hit(stageCoord.x, stageCoord.y, true)) != null) {
                currMapActor = (MapActor) stage.hit(stageCoord.x, stageCoord.y, true);
                spawnConnectDot(currMapActor);
                isDropping = true;
            }
        }

        // Animate drops
        if (isDropping) {
            if (dot != null) {
                game.batch.begin();
                game.batch.draw(dotTexture, dot.getX(), dot.getY(), dot.getWidth(), dot.getHeight());
                game.batch.end();
                dot.y -= 1000 * Gdx.graphics.getDeltaTime();
                if (dot.y < groundCoord) {
                    isDropping = false;
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {
        // Irrelevant on desktop, ignore this
    }

    @Override
    public void resume() {
        // Irrelevant on desktop, ignore this
    }

    private void createGrid(int x, int y) {
        mapActorList = new MapActor[x][y];
        int i;
        int j;
        for (j = 0; j < y; j++) {
            for (i = 0; i < x; i++) {
                mapActorList[i][j] = new MapActor(shapeRenderer, SQUARESIZE, i, j);
                stage.addActor(mapActorList[i][j]);
            }
        }
        groundCoord = Gdx.graphics.getHeight() - j * SQUARESIZE;
    }

    private void spawnConnectDot(MapActor mapActor) {
        dot = new Rectangle();
        dot.setSize(mapActor.squareSize, mapActor.squareSize);
        dot.setX(mapActor.getX());
        dot.setY(Gdx.graphics.getHeight() - (mapActor.squareSize));
    }

}

