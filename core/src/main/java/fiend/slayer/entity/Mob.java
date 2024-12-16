package fiend.slayer.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fiend.slayer.screens.GameScreen;

import java.util.Random;

public class Mob extends Entity {

    public boolean sees_player = false;
    public float speed = 1;
    public float hp = 10;

    Random rand = new Random();


    public Mob(final GameScreen gs, float tx, float ty){
        super(gs,"mob");

        sprite = new Sprite(new Texture("mob1.png"));
        autoSpriteSize();

        x = tx; y = ty;

    }

    public void update(float delta) {
        float prevX = x;
        float prevY = y;

        int xDir = rand.nextInt(3) - 2;
        int yDir = rand.nextInt(3) - 2;

        if (xDir == 1) {
            x += speed * delta;
        } else if (xDir == -1) {
            x -= speed * delta;
        }


        if (yDir == 1){
            y += speed * delta;
        } else if (yDir == -1){
            y += speed * delta;
        }


        if (gs.mapCollisionCheck(this)){
            x = prevX;
            y = prevY;
        }

        Vector2 playerPosition = new Vector2(gs.player.x, gs.player.y);
        sees_player = checkLineOfSight(new Vector2(x, y), playerPosition);

        fire_projectile();
    }

    private boolean checkLineOfSight(Vector2 start, Vector2 end) {
        MapLayer collision_layer = gs.tiledmap.getLayers().get("collisions");

        if (collision_layer == null) {
            return false;
        }

        for (RectangleMapObject object : collision_layer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Rectangle scaled_rect = new Rectangle(rect.getX() * 1/gs.tile_size,rect.getY() * 1/gs.tile_size,rect.getWidth() * 1/gs.tile_size,rect.getHeight() * 1/gs.tile_size);
            if (Intersector.intersectSegmentRectangle(start, end, scaled_rect)) {
                return false;
            }
        }

        return true;
    }

    private void fire_projectile(){
        if (sees_player) {
            Bullet b = new Bullet(gs, this, getHeading(gs.player));
            gs.bullets.add(b);
        }
    }

    public void damage(float dmg) {
        hp = Math.max(0, hp - dmg);
        if (hp <= 0) {
            dead = true;
        }
    }

    @Override
    public String toString(){
        return "MOB COORDS | X: " + x + " | Y: " + y;
    }
}
