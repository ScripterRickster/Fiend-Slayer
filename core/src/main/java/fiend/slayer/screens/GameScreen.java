package fiend.slayer.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Player;

public class GameScreen implements Screen {

    final FiendSlayer game;

    SpriteBatch batch;
    TiledMap tiledmap;
    OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/16f);
    ExtendViewport viewport;
    Player player;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        float tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        player = new Player(game);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/tile_size);
        viewport = new ExtendViewport(16, 16);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        player.update(delta);
        viewport.apply();
        viewport.getCamera().position.set(player.x, player.y, 0);
        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        //

        player.render(batch);

        //
        batch.end();

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
