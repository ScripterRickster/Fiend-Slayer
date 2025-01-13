package fiend.slayer.level;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

class Room implements Comparable<Room> {

    public int ROOM_SIZE = 15;
    public int x, y;
    public boolean uhall = false, lhall = false, rhall = false, dhall = false;
    private final int MAX_ROOM_SIDELEN = 30;
    private int tile_size = 16;

    String type;

    public Room(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    TiledMapTileLayer lay;
    TiledMapTileLayer.Cell brush;
    MapObjects colmapobjs;
    MapObjects mobmapobjs;
    public void genRoom(TiledMapTileLayer l, TiledMapTileLayer.Cell b, MapObjects c, MapObjects m) {
        lay = l; brush = b; colmapobjs = c; mobmapobjs = m;

        int cx = x + MAX_ROOM_SIDELEN/2, cy = y + MAX_ROOM_SIDELEN/2;
        int HALF = (ROOM_SIZE+1)/2;

        int ux = cx - HALF, uy = cy - HALF;

        for (int i = ux; i <= ux+ROOM_SIZE; ++i) {
            if (!(Math.abs((ux+ROOM_SIZE/2) - i) <= 3 && dhall)) setTile(i, uy);
            if (!(Math.abs((ux+ROOM_SIZE/2) - i) <= 3 && uhall)) setTile(i, uy+ROOM_SIZE);
        }

        for (int i = uy+1; i <= uy+ROOM_SIZE-1; ++i) {
            if (!(Math.abs((uy+ROOM_SIZE/2) - i) <= 3 && lhall)) setTile(ux, i);
            if (!(Math.abs((uy+ROOM_SIZE/2) - i) <= 3 && rhall)) setTile(ux+ROOM_SIZE, i);
        }

        int bridglen = MAX_ROOM_SIDELEN - ROOM_SIZE;

        if (uhall) {
            for (int i = uy + ROOM_SIZE; i <= uy + ROOM_SIZE + bridglen; ++i) {
                setTile(ux+ROOM_SIZE/2 - 3, i);
                setTile(ux+ROOM_SIZE/2 + 3, i);
            }
        }

        if (dhall) {
            for (int i = uy; i >= uy - bridglen; --i) {
                setTile(ux+ROOM_SIZE/2 - 3, i);
                setTile(ux+ROOM_SIZE/2 + 3, i);
            }
        }

        if (lhall) {
            for (int i = ux; i >= ux - bridglen; --i) {
                setTile(i, uy+ROOM_SIZE/2 - 3);
                setTile(i, uy+ROOM_SIZE/2 + 3);
            }
        }

        if (rhall) {
            for (int i = ux + ROOM_SIZE; i <= ux + ROOM_SIZE + bridglen; ++i) {
                setTile(i, uy+ROOM_SIZE/2 - 3);
                setTile(i, uy+ROOM_SIZE/2 + 3);
            }
        }

        RectangleMapObject mob_spawn = new RectangleMapObject();
        mob_spawn.getRectangle().x = (x+MAX_ROOM_SIDELEN/2)*tile_size; mob_spawn.getRectangle().y = (y+MAX_ROOM_SIDELEN/2)*tile_size;
        mobmapobjs.add(mob_spawn);

    }

    private void setTile(int x, int y) {
        lay.setCell(x, y, brush);
        RectangleMapObject hitbox = new RectangleMapObject();
        hitbox.getRectangle().x = x * tile_size; hitbox.getRectangle().y = y * tile_size;
        hitbox.getRectangle().width = tile_size; hitbox.getRectangle().height = tile_size;
        colmapobjs.add(hitbox);
    }

    @Override
    public int compareTo(Room o) {
        if (x < o.x) return -1;
        else if (x > o.x) return 1;
        else if (x == o.x) {
            if (y < o.y) return -1;
            else if (y > o.y) return 1;
        }
        return 0;
    }
}
