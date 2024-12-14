package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Player extends Entity {
    
    final FiendSlayer game;
    final GameScreen gs;

    final private Sprite sprite;
    public float x, y;

    public Player(final FiendSlayer g,final GameScreen gs) {
        super(g, gs);

        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);

        game = g;
        this.gs = gs;
        x = 0; y = 0;
    }

    @Override
    public void update(float delta) {

        float prevX = x;
        float prevY = y;


        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= delta * 4f;
        }

        if (!gs.checkForCollisions(x,y,this)){
            sprite.setPosition(x, y);
        } else {
            x = prevX;
            y = prevY;
        }

        prevX = x;
        prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= delta * 4f;
        }

        if (!gs.checkForCollisions(x,y,this)){
            sprite.setPosition(x, y);
        } else {
            x = prevX;
            y = prevY;
        }


    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
