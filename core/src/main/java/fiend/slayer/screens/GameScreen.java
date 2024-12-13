package fiend.slayer.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
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
    float tile_size;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        player = new Player(game,this);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/tile_size);
        viewport = new ExtendViewport(16, 16);



    }

    public boolean checkForCollisions(float x,float y) {
        MapObjects objects = tiledmap.getLayers().get("collisions").getObjects();
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle o_rect = rectangleObject.getRectangle();
            Rectangle c_rect = new Rectangle(o_rect.getX() * 1/tile_size, o_rect.getY() * 1/tile_size, o_rect.getWidth() * 1/tile_size, o_rect.getHeight() * 1/tile_size);


            Rectangle plr_rect = player.getRectangle();
            plr_rect.setX(x);
            plr_rect.setY(y);

            //System.out.println("RECT NAME: " + rectangleObject.getName() + " | RECT X: " + c_rect.getX() + " | RECT Y: " + c_rect.getY());
            //System.out.println("PLR RECT X: " + plr_rect.getX() + " | PLR RECT Y: " + plr_rect.getY());

            if (Intersector.overlaps(c_rect, plr_rect)) {
                return true;
            }
        }
        return false;
    }


    /*public boolean checkForCollisions(){
        int objectLayerId = 0;
        TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)tiledmap.getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects)){ //objects.getByType(RectangleMapObject.class)) {

            Rectangle rectangle = rectangleObject.getRectangle();
            Rectangle plr_rect = player.getRectangle();
            System.out.println("RECT X: " + rectangle.getX() + " | RECT Y: " + rectangle.getY());
            System.out.println("PLR RECT X: " + plr_rect.getX() + " | PLR RECT Y: " + plr_rect.getY());
            if (Intersector.overlaps(rectangle, plr_rect)) {
                return true;
            }
        }
        return false;
    }*/

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
