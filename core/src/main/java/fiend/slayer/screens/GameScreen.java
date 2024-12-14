package fiend.slayer.screens;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Mob;
import fiend.slayer.entity.Player;
import fiend.slayer.projectiles.Bullet;

public class GameScreen implements Screen {

    final FiendSlayer game;

    public SpriteBatch batch;
    public TiledMap tiledmap;
    public OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / 16f);
    public ExtendViewport viewport;

    public float tile_size;

    public Player player;

    public Array<Bullet> bullets = new Array<>();
    public Array<Mob> mobs = new Array<>();
    public Random rand = new Random();

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        player = new Player(game, this);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        if (tiledmap != null) {
            MapObjects mob_spawn_locs = tiledmap.getLayers().get("mob_spawning_locations").getObjects();
            for (RectangleMapObject rectmapobj : mob_spawn_locs.getByType(RectangleMapObject.class)) {
                Rectangle s_loc = rectmapobj.getRectangle();
                float mx = s_loc.getX() * 1 / tile_size;
                float my = s_loc.getY() * 1 / tile_size;


                System.out.println("MOB INIT CORDS | X: " + mx + " | Y: "+my);


                Mob newMob = new Mob(game,this,mx,my);
                mobs.add(newMob);
                //break;
            }
        }
    }

    public boolean checkForCollisions(Entity o) {
        if (tiledmap.getLayers().get("collisions") != null) {
            MapObjects objects = tiledmap.getLayers().get("collisions").getObjects();
            for (RectangleMapObject rectmapobj : objects.getByType(RectangleMapObject.class)) {
                Rectangle map_crect = new Rectangle(rectmapobj.getRectangle().getX() * 1 / tile_size, rectmapobj.getRectangle().getY() * 1 / tile_size,
                rectmapobj.getRectangle().getWidth() * 1 / tile_size, rectmapobj.getRectangle().getHeight() * 1 / tile_size);

                Rectangle entity_rect = o.getRectangle();
                entity_rect.setX(o.x);
                entity_rect.setY(o.y);

                if (Intersector.overlaps(map_crect, entity_rect)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(float delta) {

        // UPDATE HERE
        player.update(delta);
        for (Mob m : mobs) {
            if (rand.nextInt(100) <= 5) m.update(delta);
        }

        for (int i = bullets.size - 1; i >= 0; --i) {
            Bullet b = bullets.get(i);
            b.update(delta);
            if (b.dead) {
                bullets.removeIndex(i);
            }
        }


        // DRAW HERE
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().position.set(player.x, player.y, 0);
        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        //

        player.render(batch);

        for (Mob m : mobs) {
            m.render(batch);
        }

        for (Bullet b : bullets){
            b.render(batch);
        }

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
