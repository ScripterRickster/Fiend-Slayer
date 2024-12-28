package fiend.slayer.entity;

import java.nio.file.Files;
import java.nio.file.Path;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import fiend.slayer.screens.GameScreen;
import fiend.slayer.weapons.HeldWeapon;
import fiend.slayer.weapons.WeaponData;

public class Mob extends Entity {

    private boolean sees_player = false;

    private HeldWeapon held_weapon;
    private MobData mdata;



    public Mob(final GameScreen gs, float tx, float ty,String type){
        super(gs);

         Json json = new Json();
        try {
            mdata = json.fromJson(MobData.class, Files.readString(Path.of("entity/mobs/" + type + ".json")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        sprite = new Sprite(new Texture("entity/mobs/img/"+mdata.image+".png"));
        autoSpriteSize();
        scaleSpriteSize(mdata.img_scale);

        x = tx; y = ty;

        held_weapon = new HeldWeapon(gs, this);
        held_weapon.setWeapon(mdata.weapon);
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
        mdata.health = Math.max(0, mdata.health - dmg);
        if (mdata.health <= 0) {
            dead = true;
        }
    }

    @Override
    public String toString(){
        return "MOB COORDS | X: " + x + " | Y: " + y;
    }
}
