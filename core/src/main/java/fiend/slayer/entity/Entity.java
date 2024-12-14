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
    public String type;

    public Entity(final FiendSlayer g, final GameScreen gs,String type) {
        game = g;
        this.gs = gs;
        this.type = type;

        // subclasses are responsible for initializing x, y, and sprite
    }

    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta) {
    }

    public boolean collideWithOtherEntity(Entity e){
        return this.getRectangle().overlaps(e.getRectangle());
    }

    public float getHeadingToOtherEntity(Entity e){
        return (float) (Math.atan2(e.y-y, e.x-x));
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
