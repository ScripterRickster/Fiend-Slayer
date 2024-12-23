package fiend.slayer.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fiend.slayer.screens.GameScreen;
import fiend.slayer.weapons.HeldWeapon;

public class Mob extends Entity {

    private boolean sees_player = false;
    private float speed = 1;
    private float hp = 10;
    private HeldWeapon held_weapon;

    public Mob(final GameScreen gs, float tx, float ty){
        super(gs);

        sprite = new Sprite(new Texture("entity/red.png"));
        autoSpriteSize();

        x = tx; y = ty;

        held_weapon = new HeldWeapon(gs, this);
        held_weapon.setWeapon("shotgun");
    }

    @Override
    public void update(float delta) {

        Vector2 playerPosition = new Vector2(gs.player.center().x, gs.player.center().y);
        sees_player = checkLineOfSight(new Vector2(x, y), playerPosition);

        held_weapon.update(delta);
        if (sees_player) {
            held_weapon.fire(getHeading(gs.player));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
        held_weapon.render(batch, getHeading(gs.player));
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
