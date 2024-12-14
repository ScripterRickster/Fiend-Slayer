package fiend.slayer.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Entity {
    
    final FiendSlayer game;
    final GameScreen gs;

    final private Sprite sprite;
    public float x, y;

    public Entity(final FiendSlayer g, final GameScreen gs) {
        game = g;
        this.gs = gs;

        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);
        x = 0; y = 0;
    }

    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }

    public void update(float delta) {
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
