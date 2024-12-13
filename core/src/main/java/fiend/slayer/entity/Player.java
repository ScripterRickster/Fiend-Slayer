package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import fiend.slayer.FiendSlayer;

public class Player extends Sprite {
    final FiendSlayer game;

    float x, y;

    public Player(final FiendSlayer g) {
        super(new Texture("player.png"));

        game = g;
        x = 0; y = 0;
        setSize(1, 1);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= delta;
        }
    }

    public Rectangle getRectangle(){
        return new Rectangle(this.x,this.y,this.getWidth(),this.getHeight());
    }

    public void render() {
        draw(game.batch);
    }

}
