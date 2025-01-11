package fiend.slayer.weapons;

import com.badlogic.gdx.math.Vector2;

public class WeaponData {

    public Vector2 origin;
    public String bullet = "basic";
    public float
        fire_rate = 0,
        muzzle_boost = 0,
        bullet_count = 0,
        precision = 0,
        energy_consumption = 0,
        speed_range = 0.05f
    ;
    public boolean is_auto_fire = false;

    public WeaponData() {}

    public void scale(float denom) {
        muzzle_boost /= denom;
        origin.set(origin.x / denom, origin.y / denom);
    }
}
