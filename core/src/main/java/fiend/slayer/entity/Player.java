package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fiend.slayer.FiendSlayer;

public class Player {
    final FiendSlayer game;

    final private Sprite sprite;
    public float x, y;

    public Player(final FiendSlayer g) {
        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);

        game = g;
        x = 0; y = 0;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= delta * 4f;
        }

        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
