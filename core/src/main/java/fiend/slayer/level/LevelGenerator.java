package fiend.slayer.level;

import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelGenerator {

    private final int MAX_ROOM_SIDELEN = 30;

    TiledMap tiledmap;
    MapLayers layers;
    
    public LevelGenerator() {
    }

    public TiledMap generateLevel() {
        Random rand = new Random();

        tiledmap = new TiledMap();
        layers = tiledmap.getLayers();
        tiledmap.getProperties().put("tilewidth", 16);

        Array<Room> rooms = new Array<Room>();
        HashSet<Vector2> vis = new HashSet<Vector2>();

        Vector2 here = new Vector2(0, 0);
        vis.add(here);
        
        Room cur_room = new Room((int)here.x, (int)here.y);
        Room last_room;
        rooms.add(cur_room);

        for (int i = 0; i < 4;) {
            Vector2 next = new Vector2(here);
            last_room = cur_room;
            switch (rand.nextInt(4) + 1) {
                case 1: // UP
                    ++next.y;
                    if (vis.contains(next)) continue;
                    here = new Vector2(next); vis.add(here); ++i;
                    cur_room = new Room((int)next.x * MAX_ROOM_SIDELEN, (int)next.y * MAX_ROOM_SIDELEN);
                    rooms.add(cur_room);

                    if (last_room != null) { last_room.uhall = true; cur_room.dhall = true; }
                    break;
                case 2: // RIGHT
                    ++next.x;
                    if (vis.contains(next)) continue;
                    here = new Vector2(next); vis.add(here); ++i;
                    cur_room = new Room((int)next.x * MAX_ROOM_SIDELEN, (int)next.y * MAX_ROOM_SIDELEN);
                    rooms.add(cur_room);

                    if (last_room != null) { last_room.rhall = true; cur_room.lhall = true; }
                    break;
                case 3: // DOWN
                    --next.y;
                    if (vis.contains(next)) continue; 
                    here = new Vector2(next); vis.add(here); ++i;
                    cur_room = new Room((int)next.x * MAX_ROOM_SIDELEN, (int)next.y * MAX_ROOM_SIDELEN);
                    rooms.add(cur_room);
                    
                    if (last_room != null) { last_room.dhall = true; cur_room.uhall = true; }
                    break;
                case 4: // LEFT
                    --next.x;
                    if (vis.contains(next)) continue;
                    here = new Vector2(next); vis.add(here); ++i;
                    cur_room = new Room((int)next.x * MAX_ROOM_SIDELEN, (int)next.y * MAX_ROOM_SIDELEN);
                    rooms.add(cur_room);

                    if (last_room != null) { last_room.lhall = true; cur_room.rhall = true; }
                    break;
            }
        }

        // the points generated are the bottom-left corner of those room 'chunk's

        int lowx = Integer.MAX_VALUE, lowy = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
        for (Room r : rooms) {
            lowx = (int) Math.min(lowx, r.x);
            lowy = (int) Math.min(lowy, r.y);
            maxx = (int) Math.max(maxx, r.x + MAX_ROOM_SIDELEN);
            maxy = (int) Math.max(maxy, r.y + MAX_ROOM_SIDELEN);
        }

        lowx = -lowx + 1;
        lowy = -lowy + 1;

        maxx += lowx;
        maxy += lowy;

        for (Room r : rooms) {
            r.x += lowx; r.y += lowy;
        }

        TiledMapTileLayer lay = new TiledMapTileLayer(maxx*30, maxy*30, 16, 16); layers.add(lay);        

        MapLayer collisions_layer = new MapLayer(); collisions_layer.setName("collisions"); layers.add(collisions_layer);
        MapObjects colmapobjs = collisions_layer.getObjects();

        StaticTiledMapTile brush = new StaticTiledMapTile(new TextureRegion(new Texture("mapgen/world_1/brick.png")));
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(brush);
        for (Room room : rooms) {
            room.genRoom(lay, cell, colmapobjs);
        }

        System.out.println(colmapobjs.getCount());

        // player spawn
        MapLayer player_spawn_layer = new MapLayer(); player_spawn_layer.setName("player_spawn"); layers.add(player_spawn_layer);
        MapObjects player_spawn_layer_map_objects = player_spawn_layer.getObjects();
        RectangleMapObject player_spawn = new RectangleMapObject();
        player_spawn.getRectangle().x = lowx + (int)(MAX_ROOM_SIDELEN*0.75); player_spawn.getRectangle().y = (int)(lowy + MAX_ROOM_SIDELEN*0.75);
        player_spawn_layer_map_objects.add(player_spawn);

        return tiledmap;
    }
}
