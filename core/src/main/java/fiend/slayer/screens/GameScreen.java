package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    public Array<Loot> loot = new Array<>();
    public Array<Chest> chests = new Array<>();
    public Array<EXP_Orb> exp_orbs = new Array<>();

    public Cursor cursor;

    MapLayer drawingLayer;
    MapLayer collideLayer;
    MapLayer mobSpawnLayer;

    float font_size = 2.5f;

    Music pickup_sfx;


    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        s_render = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledmap = new LevelGenerator().generateLevel();
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);
        collideLayer = tiledmap.getLayers().get("collisions");
        drawingLayer = tiledmap.getLayers().get("main");
        mobSpawnLayer = tiledmap.getLayers().get("mob_spawn");

        player = new Player(this);

        pickup_sfx = Gdx.audio.newMusic(Gdx.files.internal("loot/sounds/pickup_item.mp3"));

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        font.getData().setScale(font_size*game.ui_scale_x,font_size*game.ui_scale_y);

        int m_curr = 0, m_limit = 10;

        MapLayer player_spawn_layer = tiledmap.getLayers().get("player_spawn");

        if (tiledmap != null) {
            MapObjects mob_spawn_locs = mobSpawnLayer.getObjects();
            for (RectangleMapObject rectmapobj : mob_spawn_locs.getByType(RectangleMapObject.class)) {
                if(m_curr+1 <= m_limit){
                    Rectangle s_loc = rectmapobj.getRectangle();
                    float mx = s_loc.getX() * 1 / tile_size;
                    float my = s_loc.getY() * 1 / tile_size;

                    //System.out.println("MOB INIT CORDS | X: " + mx + " | Y: " + my);

                    Mob newMob = new Mob(this,mx,my,"basic");
                    mobs.add(newMob);

                    m_curr++;
                    //break;
                }else{
                    break;
                }

                if (player_spawn_layer != null) {
                    RectangleMapObject loc = (RectangleMapObject) player_spawn_layer.getObjects().get(0);
                    player.x = loc.getRectangle().x;
                    player.y = loc.getRectangle().y;
                }
            }
        }

        Pixmap pixmap = new Pixmap(Gdx.files.internal("gui/crosshair.png"));

        int xHotspot = (pixmap.getWidth() + 1 )/2, yHotspot = (pixmap.getHeight() + 1)/2;
        cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);

        Chest cst = new Chest(this,5,5,player);
        chests.add(cst);
    }

    public void end_game(){
        for(Bullet bt: bullets){
            bt.disposeSounds();
            bullets.removeValue(bt, false);
        }
        mobs.clear();
        exp_orbs.clear();
        for(Chest ct: chests){
            ct.disposeSounds();
            chests.removeValue(ct, false);
        }
        loot.clear();
        player.held_weapon.disposeSounds();
        game.setScreen(new MainScreen(game,false));
        dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    public void update(float delta) {
        if(player.hp == 0){return;}
        player.update(delta);

        for(int i = mobs.size-1; i>=0; --i){
            Mob m = mobs.get(i);
            m.update(delta);
            if(m.dead){
                mobs.removeIndex(i);
            }
        }

        for (int i = bullets.size - 1; i >= 0; --i) {
            Bullet b = bullets.get(i);
            b.update(delta);
            if (b.dead) {
                b.disposeSounds();
                bullets.removeIndex(i);
            }
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
                pickup_sfx.play();
                pickup_sfx.setLooping(false);
                loot.removeValue(c_loot,false);
            }
        }

        if(player.dead){
            end_game();
            return;
        }

        if(mobs.size == 0){
            end_game();
        }
    }

    public void drawPlayerStats() {
        if(player.hp <= 0){return;}
        s_render.setProjectionMatrix(batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


        s_render.begin(ShapeRenderer.ShapeType.Line);

        float tmp_bar_height = 40f* Gdx.graphics.getWidth()/game.osx;
        float tmp_bar_width = 400f* Gdx.graphics.getHeight()/game.osy;
        float padding = 10f * Gdx.graphics.getHeight()/game.osy;

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



        float fontPaddingX = 5 * game.ui_scale_x;
        float fontPaddingY = 5 * game.ui_scale_y;

        batch.begin();
        font.draw(batch, "HP: " + (int) player.hp + " / " + (int) player.maxHP, barX + fontPaddingX, hp_barY + tmp_bar_height - fontPaddingY);
        font.draw(batch, "ARMOR: " + (int) player.armor + " / " + (int) player.maxArmor, barX + fontPaddingX, armor_barY + tmp_bar_height - fontPaddingY);
        font.draw(batch, "ENERGY: " + (int) player.energy + " / " + (int) player.maxEnergy, barX + fontPaddingX, energy_barY + tmp_bar_height - fontPaddingY);
        batch.end();


        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    public void draw() {
        if(player.dead){return;}
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
        game.update_ui_scale(width, height);
        font.getData().setScale(font_size * game.ui_scale_x,font_size*game.ui_scale_y);
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
        font.dispose();
        s_render.dispose();
        tiledmap_renderer.dispose();
        tiledmap.dispose();


        pickup_sfx.stop();
        pickup_sfx.dispose();

    }

    public Vector2 mousePos() {
        return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    }

    public boolean mapCollisionCheck(Entity o) {
        if (collideLayer != null) {
            MapObjects objects = collideLayer.getObjects();
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
