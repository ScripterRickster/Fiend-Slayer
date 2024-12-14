package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Player extends Entity {

    float speed = 8f;

    public Player(final FiendSlayer g,final GameScreen gs) {
        super(g, gs);

        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);
        x = 0; y = 0;
    }

    @Override
    public void update(float delta) {

        float prevX = x;
        float prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
        }

        if (gs.checkForCollisions(this)) {
            x = prevX;
            y = prevY;
        }

        prevX = x;
        prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed  * delta;
        }

        if (gs.checkForCollisions(this)) {
            x = prevX;
            y = prevY;
        }

        sprite.setPosition(x, y);


    }

}
