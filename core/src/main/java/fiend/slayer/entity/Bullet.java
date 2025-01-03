package fiend.slayer.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import fiend.slayer.screens.GameScreen;

public class Bullet extends Entity {

    float heading;

    public String type;
    public float speed = 25f;
    public float age;

    public Entity source_entity;

    public Bullet(final GameScreen gs, Entity se, String img_path, float heading, float muzzle_boost, float speed) {
        super(gs);

        source_entity = se;

        sprite = new Sprite(new Texture("weapons/projectiles/basic.png"));
        autoSpriteSize();

        x = source_entity.center().x - sprite.getWidth()/2; y = source_entity.center().y - sprite.getHeight()/2;

        this.heading = heading;
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setRotation(heading * MathUtils.radiansToDegrees);

        x += muzzle_boost * MathUtils.cos(heading);
        y += muzzle_boost * MathUtils.sin(heading);
        this.speed = speed;
    }

    @Override
    public void update(float delta) {
        age += delta;

        x += (float) (speed * MathUtils.cos(heading) * delta);
        y += (float) (speed * MathUtils.sin(heading) * delta);

        if (gs.mapCollisionCheck(this)) {
            dead = true;
        }

        if (!dead){
            if (source_entity instanceof Mob && this.collisionCheck(gs.player)){
               gs.player.damage(1);
               dead = true;
            } else if (source_entity instanceof Player){
                for (Mob m : gs.mobs) {
                    if (this.collisionCheck(m)) {
                        m.damage(1);
                        dead = true;
                        break;
                    }
                }
            }
        }

    }

    @Override
    public String toString(){ return "BULLET POS | X: " + x + " | Y: " + y + " | HEADING: " + heading; }
}
