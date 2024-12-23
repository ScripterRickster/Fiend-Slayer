package fiend.slayer.entity;

import fiend.slayer.screens.GameScreen;

public class BulletBuilder {

    private GameScreen gs = null;
    private Entity se = null;
    private String img_path = "weapons/projectiles/basic.png";
    private float heading = 0;
    private float muzzle_boost = 0;
    private float speed = 25f;

    public BulletBuilder(GameScreen gs, Entity se) {
        this.gs = gs;
        this.se = se;
    }

    public Bullet buildBullet() {
        return new Bullet(gs, se, img_path, heading, muzzle_boost, speed);
    }

    public BulletBuilder heading(float x) {
        this.heading = x; return this;
    }

    public BulletBuilder muzzleBoost(float x) {
        this.muzzle_boost = x; return this;
    }

    public BulletBuilder speed(float x) {
        this.speed = x; return this;
    }

    public BulletBuilder imgPath(String x) {
        this.img_path = x; return this;
    }
}
