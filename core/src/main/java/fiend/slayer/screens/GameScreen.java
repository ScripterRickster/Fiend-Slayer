package fiend.slayer.screens;

import java.util.Random;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
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
import fiend.slayer.entity.Mob;
import fiend.slayer.entity.Player;
import fiend.slayer.projectiles.Bullet;

public class GameScreen implements Screen {

    final FiendSlayer game;

    SpriteBatch batch;
    TiledMap tiledmap;
    OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1/16f);
    ExtendViewport viewport;
    public Player player;
    public float tile_size;

    public MapLayer collisionLayer;

    public Array<Bullet> bullets = new Array<>();

    Array<Mob> active_mobs = new Array<>();


    Random r = new Random();

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

        collisionLayer = tiledmap.getLayers().get("collisions");

        if(tiledmap != null){
            MapObjects mob_spawn_locs = tiledmap.getLayers().get("mob_spawning_locations").getObjects();
            for(RectangleMapObject rectangleObject: mob_spawn_locs.getByType(RectangleMapObject.class)){
                Rectangle s_loc = rectangleObject.getRectangle();
                float mx = s_loc.getX() * 1/tile_size;
                float my = s_loc.getY() * 1/tile_size;


                System.out.println("MOB INIT CORDS | X: " + mx + " | Y: "+my);


                Mob newMob = new Mob(game,this,mx,my);
                active_mobs.add(newMob);
                //break;
            }
        }
    }


    public boolean checkForCollisions(float x,float y,Object o) {
        if(collisionLayer != null){
            MapObjects objects = collisionLayer.getObjects();
            if(o != null){
                if(o.getClass() == Player.class){
                    for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                        Rectangle o_rect = rectangleObject.getRectangle();
                        Rectangle c_rect = new Rectangle(o_rect.getX() * 1/tile_size, o_rect.getY() * 1/tile_size, o_rect.getWidth() * 1/tile_size, o_rect.getHeight() * 1/tile_size);


                        Rectangle plr_rect =((Player) player).getRectangle();
                        plr_rect.setX(x);
                        plr_rect.setY(y);

                        //System.out.println("RECT NAME: " + rectangleObject.getName() + " | RECT X: " + c_rect.getX() + " | RECT Y: " + c_rect.getY());
                        //System.out.println("PLR RECT X: " + plr_rect.getX() + " | PLR RECT Y: " + plr_rect.getY());

                        if (Intersector.overlaps(c_rect, plr_rect)) {
                            return true;
                        }
                    }
                }else if(o.getClass() == Mob.class){
                    for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                        Rectangle o_rect = rectangleObject.getRectangle();
                        Rectangle c_rect = new Rectangle(o_rect.getX() * 1/tile_size, o_rect.getY() * 1/tile_size, o_rect.getWidth() * 1/tile_size, o_rect.getHeight() * 1/tile_size);


                        Rectangle mob_rect = ((Mob) o).getRectangle();
                        mob_rect.setX(x);
                        mob_rect.setY(y);

                        //System.out.println("RECT NAME: " + rectangleObject.getName() + " | RECT X: " + c_rect.getX() + " | RECT Y: " + c_rect.getY());
                        //System.out.println("PLR RECT X: " + plr_rect.getX() + " | PLR RECT Y: " + plr_rect.getY());

                        if (Intersector.overlaps(c_rect, mob_rect)) {
                            return true;
                        }
                    }
                }else if(o.getClass() == Bullet.class){
                    for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                        Rectangle o_rect = rectangleObject.getRectangle();
                        Rectangle c_rect = new Rectangle(o_rect.getX() * 1/tile_size, o_rect.getY() * 1/tile_size, o_rect.getWidth() * 1/tile_size, o_rect.getHeight() * 1/tile_size);


                        Rectangle bullet_rect = ((Bullet) o).getRectangle();
                        bullet_rect.setX(x);
                        bullet_rect.setY(y);

                        //System.out.println("RECT NAME: " + rectangleObject.getName() + " | RECT X: " + c_rect.getX() + " | RECT Y: " + c_rect.getY());
                        //System.out.println("PLR RECT X: " + plr_rect.getX() + " | PLR RECT Y: " + plr_rect.getY());

                        if (Intersector.overlaps(c_rect, bullet_rect)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void addObject(Object o){
        if(o != null){
            if(o.getClass() == Bullet.class){
                bullets.add((Bullet)o);
            }
        }
    }

    public void removeObject(Object o){
        if(o != null){
            if(o.getClass() == Bullet.class){
                for(int i=0;i<bullets.size;i++){
                    if(bullets.get(i) == (Bullet)o){
                        bullets.removeIndex(i);
                    }
                }
            }
        }
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

        for(Mob m: active_mobs){
            m.render(batch);

            if(r.nextInt(100) <= 5){
                m.update(delta);
            }
        }

        for(Bullet b: bullets){
            b.update(delta);
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
