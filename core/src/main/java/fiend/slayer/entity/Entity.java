package fiend.slayer.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public abstract class Entity {

    protected final FiendSlayer game;
    protected final GameScreen gs;
    protected Sprite sprite;

    public boolean dead = false;
    public float x, y;

    public Entity(final FiendSlayer g, final GameScreen gs) {
        game = g;
        this.gs = gs;

        // subclasses are responsible for initializing x, y, and sprite
    }

    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta) {
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
