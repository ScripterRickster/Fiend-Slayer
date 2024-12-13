package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import fiend.slayer.FiendSlayer;

public class GameScreen implements Screen {

    final FiendSlayer game;

    SpriteBatch batch;
    TiledMap tiledmap;
    OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/16f);
    ExtendViewport viewport;
    Sprite player;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        float tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        player = new Sprite(new Texture("player.png"));
        player.setSize(1,1);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/tile_size);
        viewport = new ExtendViewport(16, 16);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        movePlayer(delta);

        viewport.apply();
        viewport.getCamera().position.set(player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f, 0);

        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        player.draw(batch);

        batch.end();

    }

    public boolean checkForTiledMapCollisions(){
        int objectLayerId = 1;
        TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)tiledmap.getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();

        // there are several other types, Rectangle is probably the most common one
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, player.getRectangle())) {
                // collision happened
            }
        }
    }

    public void movePlayer(float delta) {
        float speed = 4f;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setPosition(player.getX(), player.getY() + speed * delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setPosition(player.getX(), player.getY() - speed * delta);

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.setPosition(player.getX() - speed * delta, player.getY());
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setPosition(player.getX() + speed * delta, player.getY());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
