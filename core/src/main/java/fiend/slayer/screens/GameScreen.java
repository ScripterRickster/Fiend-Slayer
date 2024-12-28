package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
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

public class GameScreen implements Screen {

    final FiendSlayer game;
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
    MapLayer mobSpawnLayer;

    public GameScreen(final FiendSlayer g) {
        game = g;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tiledmap = new TmxMapLoader().load("tiledmaps/bluelevel.tmx");
        tile_size = tiledmap.getProperties().get("tilewidth", Integer.class);

        drawingLayer = tiledmap.getLayers().get("main");
        collideLayer = tiledmap.getLayers().get("collisions");
        mobSpawnLayer = tiledmap.getLayers().get("mobspawn");

//        createNewMap();

        player = new Player(this);

        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);

        int m_curr = 0, m_limit = 10;

        if (tiledmap != null) {
            MapObjects mob_spawn_locs = mobSpawnLayer.getObjects();
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

        // crosshair stuff
        Pixmap pixmap = new Pixmap(Gdx.files.internal("gui/crosshair.png"));
        int xHotspot = (pixmap.getWidth() + 1) / 2, yHotspot = (pixmap.getHeight() + 1) / 2;
        cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose();
        Gdx.graphics.setCursor(cursor);
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

        if (player.hp <= 0) {
            bullets.clear();
            mobs.clear();
            game.setScreen(new EndScreen(game));
            dispose();
        }
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
        }

        player.draw(batch);

        batch.end(); //

        player.drawStats(batch); // uses its own batches

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

            Rectangle map_crect = new Rectangle(rectmapobj.getRectangle().getX() * 1 / tile_size, rectmapobj.getRectangle().getY() * 1 / tile_size,
                rectmapobj.getRectangle().getWidth() * 1 / tile_size, rectmapobj.getRectangle().getHeight() * 1 / tile_size);

            Rectangle entity_rect = o.getRectangle();
            entity_rect.setX(o.x);
            entity_rect.setY(o.y);

            if (Intersector.overlaps(map_crect, entity_rect)) {
                return true;
            }
        }

        return false;
    }

    public void createNewMap() {
        if (tiledmap != null) {
            for (TiledMapTileSet r : tiledmap.getTileSets()) {
                System.out.println(r.getName());
            }

            TiledMapTileSet m_ts = tiledmap.getTileSets().getTileSet("tileset1");
            System.out.println(m_ts);
            TiledMapTile walls = m_ts.getTile(1);

            TiledMapTileLayer d_layer = (TiledMapTileLayer) drawingLayer;

            int boxSize = 25;
            int offsetX = -boxSize / 2;
            int offsetY = -boxSize / 2;

            for (int i = 0; i < offsetX + boxSize - 1; i++) {
                for (int j = 0; j < offsetY + boxSize - 1; j++) {
                    RectangleMapObject m_block = new RectangleMapObject();
                    m_block.getRectangle().set(i * tile_size, j * tile_size, tile_size, tile_size);
                    mobSpawnLayer.getObjects().add(m_block);
                }
            }

            for (int i = offsetX; i < offsetX + boxSize; i++) {
                for (int j = offsetY; j < offsetY + boxSize; j++) {
                    if (i == offsetX || i == offsetX + boxSize - 1 || j == offsetY || j == offsetY + boxSize - 1) {
                        TiledMapTileLayer.Cell nCell = new TiledMapTileLayer.Cell();
                        nCell.setTile(walls);
                        d_layer.setCell(i, j, nCell);

                        RectangleMapObject c_block = new RectangleMapObject();
                        c_block.getRectangle().set(i * tile_size, j * tile_size, tile_size, tile_size);
                        collideLayer.getObjects().add(c_block);
                    }
                }
            }
        }
    }

}
