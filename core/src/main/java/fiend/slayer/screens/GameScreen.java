package fiend.slayer.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    ShapeRenderer s_render;

    final FiendSlayer game;

    BitmapFont font;

    public SpriteBatch batch;
    public TiledMap tiledmap;
    public OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / 16f);
    public ExtendViewport viewport;

    public float tile_size;

    public Player player;

    public Array<Bullet> bullets = new Array<>();
    public Array<Mob> mobs = new Array<>();
    public Random rand = new Random();

    float barWidth = 20f; float barHeight = 10f;
    float font_size = 2f;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {


        s_render = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        player = new Player(game, this);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(font_size);

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


    public void drawPlayerStats() {
        s_render.setProjectionMatrix(batch.getProjectionMatrix());
        batch.end();


        s_render.setProjectionMatrix(batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


        s_render.begin(ShapeRenderer.ShapeType.Line);

        float tmp_bar_height = 40f;
        float tmp_bar_width = 400f;
        float padding = 10f;

        float barX = padding;
        float hp_barY = Gdx.graphics.getHeight() - tmp_bar_height - padding; // Y position from top
        float armor_barY = hp_barY - tmp_bar_height - padding;
        float energy_barY = armor_barY - tmp_bar_height - padding;


        s_render.setColor(new Color(236 / 255f, 236 / 255f, 236 / 255f, 1f));
        s_render.rect(barX, hp_barY, tmp_bar_width, tmp_bar_height);

        s_render.rect(barX,armor_barY,tmp_bar_width,tmp_bar_height);

        s_render.rect(barX,energy_barY,tmp_bar_width,tmp_bar_height);


        s_render.end();
        s_render.begin(ShapeRenderer.ShapeType.Filled);

        if(player.hp > 0){
            float hpPercentage = (float)player.hp / (float)player.maxHP;
            float hpBarWidth = tmp_bar_width * hpPercentage;
            s_render.setColor(1,0,0,1);
            s_render.rect(barX+1, hp_barY+1, hpBarWidth-1, tmp_bar_height-1);
        }

        if(player.armor > 0){
            float armorPercentage = (float)player.armor / (float)player.maxArmor;
            float armorBarWidth = tmp_bar_width * armorPercentage;
            s_render.setColor(139/255f,137/255f,137/255f,1);
            s_render.rect(barX+1, armor_barY+1, armorBarWidth-1, tmp_bar_height-1);
        }

        if(player.energy > 0){
            float energyPercentage = (float)player.energy / (float)player.maxEnergy;
            float energyBarWidth = tmp_bar_width * energyPercentage;
            s_render.setColor(21/255f,109/255f,240/255f,1);
            s_render.rect(barX+1, energy_barY+1, energyBarWidth-1, tmp_bar_height-1);
        }

        s_render.end();

        batch.begin();

        font.draw(batch, "HP: " + (int) player.hp + " / " + (int) player.maxHP, barX + 5, hp_barY + tmp_bar_height - 5);
        font.draw(batch, "ARMOR: " + (int) player.armor + " / " + (int) player.maxArmor, barX + 5, armor_barY + tmp_bar_height - 5);
        font.draw(batch, "ENERGY: " + (int) player.energy + " / " + (int) player.maxEnergy, barX + 5, energy_barY + tmp_bar_height - 5);



        batch.end();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
    }


    @Override
    public void render(float delta) {

        // UPDATE HERE
        player.update(delta);
        for(int i=mobs.size-1;i>=0;--i){
            Mob m = mobs.get(i);
            if (rand.nextInt(100) <= 5) m.update(delta);
            if(m.dead){
                mobs.removeIndex(0);
            }
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

        drawPlayerStats();
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
    public void dispose() { // this is not called automatically
    }

}
