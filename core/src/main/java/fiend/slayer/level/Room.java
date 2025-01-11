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

    public Room(int x, int y) {
        this.x = x;
        this.y = y;
    }

    TiledMapTileLayer lay;
    TiledMapTileLayer.Cell brush;
    MapObjects colmapobjs;
    public void genRoom(TiledMapTileLayer l, TiledMapTileLayer.Cell b, MapObjects c) {
        lay = l; brush = b; colmapobjs = c;

        int cx = x + MAX_ROOM_SIDELEN/2, cy = y + MAX_ROOM_SIDELEN/2;

        for (int i = cx; i <= cx+ROOM_SIZE; ++i) {
            if (!(Math.abs((cx+ROOM_SIZE/2) - i) <= 3 && dhall)) setTile(i, cy);
            if (!(Math.abs((cx+ROOM_SIZE/2) - i) <= 3 && uhall)) setTile(i, cy+ROOM_SIZE);
        }

        for (int i = cy+1; i <= cy+ROOM_SIZE-1; ++i) {
            if (!(Math.abs((cy+ROOM_SIZE/2) - i) <= 3 && lhall)) setTile(cx, i);
            if (!(Math.abs((cy+ROOM_SIZE/2) - i) <= 3 && rhall)) setTile(cx+ROOM_SIZE, i);
        }

        int bridglen = MAX_ROOM_SIDELEN - ROOM_SIZE;

        if (uhall) {
            for (int i = cy + ROOM_SIZE; i <= cy + ROOM_SIZE + bridglen; ++i) {
                setTile(cx+ROOM_SIZE/2 - 3, i);
                setTile(cx+ROOM_SIZE/2 + 3, i);
            }
        }

        if (dhall) {
            for (int i = cy; i >= cy - bridglen; --i) {
                setTile(cx+ROOM_SIZE/2 - 3, i);
                setTile(cx+ROOM_SIZE/2 + 3, i);
            }
        }

        if (lhall) {
            for (int i = cx; i >= cx - bridglen; --i) {
                setTile(i, cy+ROOM_SIZE/2 - 3);
                setTile(i, cy+ROOM_SIZE/2 + 3);
            }
        }

        if (rhall) {
            for (int i = cx + ROOM_SIZE; i <= cx + ROOM_SIZE + bridglen; ++i) {
                setTile(i, cy+ROOM_SIZE/2 - 3);
                setTile(i, cy+ROOM_SIZE/2 + 3);
            }
        }

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
