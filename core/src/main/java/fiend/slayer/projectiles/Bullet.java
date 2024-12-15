package fiend.slayer.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Mob;
import fiend.slayer.entity.Player;
import fiend.slayer.screens.GameScreen;

public class Bullet extends Entity {

    float heading;

    public String type;
    public float speed = 10;
    public float age;

    public Entity source_entity;

    public Bullet(final GameScreen gs, Entity se, float heading) {
        super(gs,"bullet");

        source_entity = se;

        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(1, 1);

        x = source_entity.x; y = source_entity.y;
        this.heading = heading;
        sprite.setOrigin(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
        sprite.setRotation(heading * MathUtils.radiansToDegrees);
        sprite.setPosition(x, y);

        //System.out.printf("Bullet X %f Y %f Source X %f Y %f\n", x, y, source_entity.x, source_entity.y);
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

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        if ((x < 0 || x > 800 * (1/gs.tile_size)) || (y < 0 || y > 600 * (1/gs.tile_size))){
            dead = true;
        }

        sprite.setPosition(x, y);
    }

    @Override
    public String toString(){ return "BULLET POS | X: " + x + " | Y: " + y + " | HEADING: " + heading; }
}
