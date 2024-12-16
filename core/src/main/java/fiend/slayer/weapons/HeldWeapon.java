package fiend.slayer.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fiend.slayer.entity.Bullet;
import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;

public class HeldWeapon {

    final GameScreen gs;

    public Entity holder;
    private Sprite sprite;

    public HeldWeapon(final GameScreen gs, final Entity holder) {
        this.gs = gs;
        this.holder = holder;

        sprite = new Sprite(new Texture("weapons/bad pistol.png"));
        sprite.setSize(sprite.getWidth() / gs.tile_size, sprite.getHeight() / gs.tile_size);
    }

    public void render(SpriteBatch batch) {
        float angle = MathUtils.atan2(gs.mousePos().y - holder.center().y, gs.mousePos().x - holder.center().x);

        sprite.setOrigin(holder.center().x - sprite.getWidth() / 2, holder.center().y - sprite.getHeight() / 2);
        if (-MathUtils.HALF_PI < angle && angle <= MathUtils.HALF_PI) {
            sprite.setFlip(false, false);
            sprite.setOrigin(0, sprite.getHeight()/2);
            sprite.setRotation(angle * MathUtils.radiansToDegrees);
            sprite.setPosition(holder.center().x, holder.y);
        } else {
            sprite.setFlip(true, false);
            sprite.setOrigin(sprite.getWidth(), sprite.getHeight()/2);
            angle += MathUtils.PI;
            sprite.setRotation(angle * MathUtils.radiansToDegrees);
            sprite.setPosition(holder.center().x - sprite.getWidth(), holder.y);
        }

//        System.out.printf("HeldWeapon Pos: %f %f\n", sprite.getX(), sprite.getY());

        sprite.draw(batch);
    }

    public void fire() {
        Vector2 mpos = gs.mousePos();
        float angle = (float) MathUtils.atan2(mpos.y - holder.center().y, mpos.x - holder.center().x);
        Bullet b = new Bullet(gs, holder, angle);
        gs.bullets.add(b);
    }

}
