package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Bullet;
import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Mob;
import fiend.slayer.entity.Player;
import fiend.slayer.level.LevelGenerator;
import fiend.slayer.loot.Chest;
import fiend.slayer.loot.EXP_Orb;
import fiend.slayer.loot.Loot;

import java.util.Random;

public class GameScreen implements Screen {

    final FiendSlayer game;

    private SpriteBatch batch;
    private TiledMap tiledmap;
    private MapLayers maplayers;
    private OrthogonalTiledMapRenderer tiledmap_renderer;
    private ExtendViewport viewport;
    private Array<Bullet> bullets = new Array<>();
    private Cursor cursor;
    private MapLayer collideLayer;

    public Player player;
    public Array<Mob> mobs = new Array<>();
    public float tile_size;
    public Array<Loot> loot = new Array<>();
    public Array<Chest> chests = new Array<>();
    public Array<EXP_Orb> exp_orbs = new Array<>();

    public Cursor cursor;

    MapLayer drawingLayer;
    MapLayer collideLayer;
    MapLayer mobSpawnLayer;

    float osx,osy,scaleFactor;
    float font_size = 2.5f;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        
        tiledmap = new LevelGenerator().generateLevel();
        // tiledmap = new TmxMapLoader().load("tiledmaps/bluelevel.tmx");
        maplayers = tiledmap.getLayers();
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);
        collideLayer = maplayers.get("collisions");

        player = new Player(this);



        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        int m_curr = 0, m_limit = 10;
        MapLayer mob_spawn_layer = maplayers.get("mob_spawn");
        MapLayer player_spawn_layer = maplayers.get("player_spawn");

        if (tiledmap != null) {
            if (mob_spawn_layer != null) {
                MapObjects mob_spawn_locs = mob_spawn_layer.getObjects();
                for (RectangleMapObject rectmapobj : mob_spawn_locs.getByType(RectangleMapObject.class)) {
                    if (m_curr + 1 <= m_limit) {
                        Rectangle s_loc = rectmapobj.getRectangle();
                        float mx = s_loc.getX() * 1 / tile_size;
                        float my = s_loc.getY() * 1 / tile_size;

                        //System.out.println("MOB INIT CORDS | X: " + mx + " | Y: " + my);

                        Mob newMob = new Mob(this, mx, my, "basic");
                        mobs.add(newMob);

                        m_curr++;
                        //break;
                    } else {
                        break;
                    }
                }
            }

            if (player_spawn_layer != null) {
                RectangleMapObject loc = (RectangleMapObject) player_spawn_layer.getObjects().get(0);
                player.setPos(loc.getRectangle().x, loc.getRectangle().y);
            }
       }

        // crosshair stuff
        Pixmap pixmap = new Pixmap(Gdx.files.internal("gui/crosshair.png"));

        int xHotspot = (pixmap.getWidth() + 1 )/2, yHotspot = (pixmap.getHeight() + 1)/2;
        cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);

        Chest cst = new Chest(this,5,5,player);
        chests.add(cst);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    public void update(float delta) {
        player.update(delta);

        for (int i = mobs.size - 1; i >= 0; --i) {
            Mob m = mobs.get(i);
            m.update(delta);
            if (m.dead) {
                mobs.removeIndex(i);
            }
        }

        for (int i = bullets.size - 1; i >= 0; --i) {
            Bullet b = bullets.get(i);
            b.update(delta);
            if (b.dead) {
                bullets.removeIndex(i);
            }
        }
      
        if(player.hp <= 0){
            bullets.clear();
            mobs.clear();
            game.setScreen(new EndScreen(game));
            dispose();
        }

        for (Chest c: chests) {
            c.update(delta);

            if (c.getAlpha() <=0) {
                chests.removeValue(c,false);
            }
        }

        for (EXP_Orb exp: exp_orbs) {
            exp.update(delta);
        }

        if(Gdx.input.isButtonJustPressed(Buttons.RIGHT)){
            float c_dist = Float.MAX_VALUE;
            Loot c_loot = null;
            for(Loot l: loot){
                float l_dst = l.distanceToEntity(player);
                if(l_dst < c_dist && l_dst <= player.pickup_range / tile_size ){
                    c_loot = l;
                    c_dist = l_dst;
                }
            }

            if(c_loot != null){
                if(c_loot.r_class == "weapon"){
                    loot.add(new Loot(this,player.x,player.y,"weapon",player.getCurrentWeapon()));
                    player.changeWeapon(c_loot.r_type);
                }else if(c_loot.r_class == "potion"){
                    if(c_loot.r_type == "hp_potion"){
                        player.hp = Math.min(player.hp+2,player.maxHP);
                    }else if(c_loot.r_type == "energy_potion"){
                        player.energy = Math.min(player.energy+50,player.maxEnergy);
                    }
                }
                loot.removeValue(c_loot,false);
            }
        }
    }

    public void drawPlayerStats() {
        s_render.setProjectionMatrix(batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


        s_render.begin(ShapeRenderer.ShapeType.Line);

        float tmp_bar_height = 40f* Gdx.graphics.getWidth()/osx;
        float tmp_bar_width = 400f* Gdx.graphics.getHeight()/osy;
        float padding = 10f * Gdx.graphics.getHeight()/osy;

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

        if (!player.isAlive()) {
            bullets.clear();
            mobs.clear();
            game.setScreen(new EndScreen(game));
            dispose();
        }

        s_render.end();



        float fontPadding = 5 * scaleFactor;

        batch.begin();
        font.draw(batch, "HP: " + (int) player.hp + " / " + (int) player.maxHP, barX + fontPadding, hp_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ARMOR: " + (int) player.armor + " / " + (int) player.maxArmor, barX + fontPadding, armor_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ENERGY: " + (int) player.energy + " / " + (int) player.maxEnergy, barX + fontPadding, energy_barY + tmp_bar_height - fontPadding);
        batch.end();


        batch.setProjectionMatrix(viewport.getCamera().combined);

    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().position.set(player.x, player.y, 0);

        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin(); //

        for (Mob m : mobs) {
            m.draw(batch);
        }

        for (Bullet b : bullets) {
            b.draw(batch);
          
        for (Chest c: chests) {
            c.draw(batch);
        }

        for (Loot l: loot) {
            l.draw(batch);
        }

        for (EXP_Orb exp: exp_orbs) {
            exp.draw(batch);
        }

        player.draw(batch);

        batch.end();

        drawPlayerStats();

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
        batch.dispose();
        cursor.dispose();
    }

    public Vector2 mousePos() {
        return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    }

    public boolean mapCollisionCheck(Entity o) {
        if (collideLayer == null) return false;

        MapObjects objects = collideLayer.getObjects();
        for (RectangleMapObject rectmapobj : objects.getByType(RectangleMapObject.class)) {

            Rectangle map_crect = new Rectangle(rectmapobj.getRectangle().getX() / tile_size, rectmapobj.getRectangle().getY() / tile_size,
                rectmapobj.getRectangle().getWidth() / tile_size, rectmapobj.getRectangle().getHeight() / tile_size);

            Rectangle entity_rect = o.getRectangle();
            entity_rect.setX(o.x);
            entity_rect.setY(o.y);

            if (Intersector.overlaps(map_crect, entity_rect)) {
                return true;
            }
        }

        return false;
    }


    // public access methods

    public TiledMap getTiledMap() { return tiledmap; }
    public Array<Bullet> getBullets() { return bullets; }
}
