package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import fiend.slayer.screens.GameScreen;
import fiend.slayer.weapons.HeldWeapon;

public class Player extends Entity {

    private float maxHP = 10;
    private float maxArmor = 10;
    private float maxEnergy = 10;
    private float hp = maxHP;
    private float armor = maxArmor;
    private float energy = maxEnergy;
    private boolean dead = false;
    private HeldWeapon held_weapon;
    private float ui_scale_factor;
    private float font_size = 2.5f;
    private BitmapFont font;
    private float speed = 8f;
    private Task regenArmor;
    private ShapeRenderer shape_renderer = new ShapeRenderer();

    public Player(final GameScreen gs) {
        super(gs);

        sprite = new Sprite(new Texture("entity/blue.png"));
        autoSpriteSize();

        x = 0;
        y = 0;

        regenArmor = new Task() {
            @Override
            public void run() {
                if (!dead && armor < maxArmor) {
                    armor++;
                    System.out.println("Armor Increased");
                }
            }
        };
        Timer.schedule(regenArmor, 0f, 15f);

        held_weapon = new HeldWeapon(gs, this);
      
        held_weapon.setWeapon("ak-47");
    }

    public String getCurrentWeapon(){return held_weapon.getCurrentWeapon();}

    public void changeWeapon(String wpn){
        if(wpn != null){
            held_weapon.setWeapon(wpn);
        }
    }

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
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 mpos = gs.mousePos();
            float angle = MathUtils.atan2(mpos.y - center().y, mpos.x - center().x);
            held_weapon.fire(angle);
        }

        if (gs.mapCollisionCheck(this)) {
            x = prevX;
            y = prevY;
        }

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

    public void drawStats(SpriteBatch batch) {

        shape_renderer.setProjectionMatrix(batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        shape_renderer.begin(ShapeRenderer.ShapeType.Filled);

        float tmp_bar_height = 40f;
        float tmp_bar_width = 400f;
        float padding = 10f;

        float hp_barY = Gdx.graphics.getHeight() - tmp_bar_height - padding; // Y position from top
        float armor_barY = hp_barY - tmp_bar_height - padding;
        float energy_barY = armor_barY - tmp_bar_height - padding;

        shape_renderer.setColor(new Color(100 / 255f, 100 / 255f, 100 / 255f, 1f));
        shape_renderer.rect(padding, hp_barY, tmp_bar_width, tmp_bar_height);
        shape_renderer.rect(padding, armor_barY, tmp_bar_width, tmp_bar_height);
        shape_renderer.rect(padding, energy_barY, tmp_bar_width, tmp_bar_height);

        float hp_bar_width = tmp_bar_width * (hp / maxHP);
        shape_renderer.setColor(1, 0, 0, 1);
        shape_renderer.rect(padding + 1, hp_barY + 1, hp_bar_width - 2, tmp_bar_height - 2);

        float armor_bar_width = tmp_bar_width * (armor / maxArmor);
        shape_renderer.setColor(139 / 255f, 137 / 255f, 137 / 255f, 1);
        shape_renderer.rect(padding + 1, armor_barY + 1, armor_bar_width - 2, tmp_bar_height - 2);

        float energy_bar_width = tmp_bar_width * (energy / maxEnergy);
        shape_renderer.setColor(21 / 255f, 109 / 255f, 240 / 255f, 1);
        shape_renderer.rect(padding + 1, energy_barY + 1, energy_bar_width - 2, tmp_bar_height - 2);

        shape_renderer.end();

        batch.begin(); //

        float fontPadding = 5 * ui_scale_factor;

        font.draw(batch, "HP: " + (int) hp + " / " + (int) maxHP, padding + fontPadding, hp_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ARMOR: " + (int) armor + " / " + (int) maxArmor, padding + fontPadding, armor_barY + tmp_bar_height - fontPadding);
        font.draw(batch, "ENERGY: " + (int) energy + " / " + (int) maxEnergy, padding + fontPadding, energy_barY + tmp_bar_height - fontPadding);

        font.draw(batch, "pos: " + x + " " + y, 50, 50);

        batch.end(); //
    }

    @Override
    public String toString() {
        return "Player Info | CORDS -> X: " + x + " , Y: " + y + " ; Stats -> HP: " + hp + " -> Armor: " + armor + " -> Energy Remaining: " + energy;
    }

    public void setPos(float x, float y) { this.x = x; this.y = y; }

}
