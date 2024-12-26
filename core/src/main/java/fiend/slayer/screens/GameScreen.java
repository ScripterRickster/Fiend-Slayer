package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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

import java.util.Random;

public class GameScreen implements Screen {

    ShapeRenderer s_render;

    final FiendSlayer game;

    BitmapFont font;

    public SpriteBatch batch;
    public TiledMap tiledmap;
    public OrthogonalTiledMapRenderer tiledmap_renderer;
    public ExtendViewport viewport;

    public float tile_size;

    public Player player;
    public Array<Bullet> bullets = new Array<>();
    public Array<Mob> mobs = new Array<>();

    public Cursor cursor;

    MapLayer drawingLayer;
    MapLayer collideLayer;

    float osx,osy,scaleFactor;
    float font_size = 3f;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        s_render = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("bluelevel.tmx");
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);
        collideLayer = tiledmap.getLayers().get("collisions");
        drawingLayer = tiledmap.getLayers().get("main");

        player = new Player(this);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        osx = Gdx.graphics.getWidth();
        osy = Gdx.graphics.getHeight();
        scaleFactor = 1f;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(font_size);

        if (tiledmap != null) {
            MapObjects mob_spawn_locs = tiledmap.getLayers().get("mob_spawning_locations").getObjects();
            for (RectangleMapObject rectmapobj : mob_spawn_locs.getByType(RectangleMapObject.class)) {
                Rectangle s_loc = rectmapobj.getRectangle();
                float mx = s_loc.getX() * 1 / tile_size;
                float my = s_loc.getY() * 1 / tile_size;

                System.out.println("MOB INIT CORDS | X: " + mx + " | Y: " + my);

                Mob newMob = new Mob(this,mx,my);
                mobs.add(newMob);
                //break;
            }
        }

        Pixmap pixmap = new Pixmap(Gdx.files.internal("gui/crosshair.png"));
        // Set hotspot to the middle of it (0,0 would be the top-left corner)
        int xHotspot = (pixmap.getWidth() + 1 )/2, yHotspot = (pixmap.getHeight() + 1)/2;
        cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose(); // We don't need the pixmap anymore
        Gdx.graphics.setCursor(cursor);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    public void update(float delta) {
        player.update(delta);

        for(int i = mobs.size-1; i>=0; --i){
            Mob m = mobs.get(i);
            if (new Random().nextInt(100) <= 5) m.update(delta);
            if(m.dead){
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
    }

    public void drawPlayerStats() {
        s_render.setProjectionMatrix(batch.getProjectionMatrix());
        batch.end();


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



        float fontPadding = 5 * scaleFactor;

        font.draw(batch, "HP: " + (int) player.hp + " / " + (int) player.maxHP, barX + fontPadding, hp_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ARMOR: " + (int) player.armor + " / " + (int) player.maxArmor, barX + fontPadding, armor_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ENERGY: " + (int) player.energy + " / " + (int) player.maxEnergy, barX + fontPadding, energy_barY + tmp_bar_height - fontPadding);



        batch.end();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().position.set(player.x, player.y, 0);

        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        //

        for (Bullet b : bullets){
            b.draw(batch);
        }

        for (Mob m : mobs) {
            m.draw(batch);
        }

        player.draw(batch);

        drawPlayerStats();
        //
        batch.end();

        if(player.hp <= 0){
            bullets.clear();
            mobs.clear();
            game.setScreen(new EndScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        scaleFactor = height/osy;
        font.getData().setScale(font_size * scaleFactor);
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
        batch.dispose();
        cursor.dispose();
    }

    public Vector2 mousePos() {
        return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    }

    public boolean mapCollisionCheck(Entity o) {
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
}
