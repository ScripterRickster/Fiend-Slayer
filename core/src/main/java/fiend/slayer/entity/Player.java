package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import fiend.slayer.screens.GameScreen;
import fiend.slayer.weapons.HeldWeapon;

public class Player extends Entity {

    float speed = 8f;

    public float maxHP = 10;
    public float maxArmor = 10;
    public float maxEnergy = 10;

    public float hp = maxHP;
    public float armor = maxArmor;
    public float energy = maxEnergy;


    public final float pickup_range = 20; // in pixels

    public boolean dead = false;

    Task regenArmor;

    public HeldWeapon held_weapon;

    public Player(final GameScreen gs) {
        super(gs);

        sprite = new Sprite(new Texture("entity/blue.png"));
        autoSpriteSize();

        x = 0; y = 0;

        regenArmor = new Task(){
            @Override
            public void run(){
                if(!dead && armor < maxArmor){
                    armor++;
                    System.out.println("Armor Increased");
                }
            }
        };
        Timer.schedule(regenArmor,0f,15f);

        held_weapon = new HeldWeapon(gs, this);
        held_weapon.setWeapon("ak-47");
    }

    public String getCurrentWeapon(){return held_weapon.getCurrentWeapon();}

    public void changeWeapon(String wpn){
        if(wpn != null){
            held_weapon.setWeapon(wpn);
        }
    }

    int clicks = 0;
    @Override
    public void update(float delta) {
        if (dead) {
            regenArmor.cancel();
            return;
        }

        float prevX = x;
        float prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
        }

        if (gs.mapCollisionCheck(this)) {
            x = prevX;
            y = prevY;
        }

        prevX = x;
        prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed * delta;
        }

        held_weapon.update(delta);
        if(held_weapon.getCurrentWeaponData().is_auto){
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                fire_current_weapon();
            }
        }else{
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                fire_current_weapon();
            }
        }


        if (gs.mapCollisionCheck(this)) {
            x = prevX;
            y = prevY;
        }

    }

    public void fire_current_weapon(){
        Vector2 mpos = gs.mousePos();
        float angle = MathUtils.atan2(mpos.y - center().y, mpos.x - center().x);
        held_weapon.fire(angle);
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
        held_weapon.render(batch, MathUtils.atan2(gs.mousePos().y - center().y, gs.mousePos().x - center().x));
    }

    public void damage(float dmg) {
        if (armor > 0) {
            armor = Math.max(0, armor - dmg);
        } else {
            hp = Math.max(0, hp - dmg);
        }

        if (hp <= 0) {
            dead = true;
        }
    }

    @Override
    public String toString(){
        return "Player Info | CORDS -> X: " + x + " , Y: " + y + " ; Stats -> HP: " + hp + " -> Armor: " + armor + " -> Energy Remaining: " + energy;
    }

}
