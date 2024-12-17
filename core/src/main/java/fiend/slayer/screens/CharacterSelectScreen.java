package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Player;

public class CharacterSelectScreen implements Screen {

    final FiendSlayer game;

    //SpriteBatch batch;

    public float tile_size;
    public TiledMap tiledmap;
    public OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / 16f);
    public ExtendViewport viewport;

    MapProperties map_prop;
    public int map_x,map_y;

    public BitmapFont font;


    public CharacterSelectScreen(final FiendSlayer g) {
        this.game = g;

        font = new BitmapFont();
        tiledmap = new TmxMapLoader().load("character_selection.tmx");
        map_prop = tiledmap.getProperties();

        map_x = map_prop.get("width",Integer.class);
        map_y = map_prop.get("height",Integer.class);



        tile_size = map_prop.get("tilewidth", Integer.class);
        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().position.set(map_x/2,map_y/2,0);
        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.println("hi");
        }

        if (Gdx.input.isTouched()) {
            //game.setScreen(new GameScreen(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    public void dispose() { // this is not called automatically
    }
}
