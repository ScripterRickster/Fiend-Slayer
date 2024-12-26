package fiend.slayer.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import fiend.slayer.entity.BulletBuilder;
import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class HeldWeapon {

    private final GameScreen gs;

    private Sprite sprite;
    private Entity holder;
    private WeaponData wdata = new WeaponData();
    private String weapon_id = null;

    public HeldWeapon(final GameScreen gs, final Entity holder) {
        this.gs = gs;
        this.holder = holder;
    }

    public void setWeapon(String wid) {
        this.weapon_id = wid;

        sprite = new Sprite(new Texture("weapons/img/" + weapon_id + ".png"));
        sprite.setSize(sprite.getWidth() / gs.tile_size, sprite.getHeight() / gs.tile_size);

        Json json = new Json();
        try {
            wdata = json.fromJson(WeaponData.class, Files.readString(Path.of("assets/weapons/" + weapon_id + ".json")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        wdata.scale(gs.tile_size);
    }

    private float time_since_last_fire = 0;

    public void update(float delta) {
        if (weapon_id == null) return;
        time_since_last_fire += delta;
    }

    public void render(SpriteBatch batch, float angle) {
        if (weapon_id == null) return;

        if (-MathUtils.HALF_PI < angle && angle <= MathUtils.HALF_PI) {
            sprite.setFlip(false, false);
            sprite.setOrigin(wdata.origin.x, wdata.origin.y);
            sprite.setPosition(holder.center().x, holder.center().y - sprite.getHeight()/2);
        } else {
            sprite.setFlip(true, false);
            angle += MathUtils.PI;
            sprite.setOrigin(sprite.getWidth() - wdata.origin.x, wdata.origin.y);
            sprite.setPosition(holder.center().x - sprite.getWidth(), holder.center().y - sprite.getHeight()/2);
        }
        sprite.setRotation(angle * MathUtils.radiansToDegrees);

//        System.out.printf("HeldWeapon Pos: %f %f\n", sprite.getX(), sprite.getY());

        sprite.draw(batch);
    }


    public void fire(float angle) {
        if (weapon_id == null) return;

        Random rand = new Random();

        if (time_since_last_fire < wdata.fire_rate) return;
        time_since_last_fire = 0;

        for (int i = 0; i < wdata.bullet_count; ++i) {
            gs.bullets.add(new BulletBuilder(gs, holder)
                    .heading(angle + wdata.precision * rand.nextFloat(-1, 1))
                    .muzzleBoost(wdata.muzzle_boost)
                    .speed(25f * rand.nextFloat(1 - wdata.speed_range, 1 + wdata.speed_range))
                    .imgPath("weapons/projectiles/basic.png")
                .buildBullet()
            );
        }
    }
}
