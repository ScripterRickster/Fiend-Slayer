package fiend.slayer.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fiend.slayer.screens.GameScreen;

public abstract class Entity {

    protected final GameScreen gs;
    protected Sprite sprite;

    public boolean dead = false;
    public float x, y;

    public Entity(final GameScreen gs) {
        this.gs = gs;

        // subclasses are responsible for initializing x, y, and sprite
    }

    public void update(float delta) {
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public void autoSpriteSize() { sprite.setSize(sprite.getWidth() / gs.tile_size, sprite.getHeight() / gs.tile_size); }
    public void scaleSpriteSize(float scale) {sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale); }

    public Rectangle getRectangle(){ return new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()); }

    public boolean collisionCheck(Entity e){
        return this.getRectangle().overlaps(e.getRectangle());
    }

    public float getHeading(Entity e) { return (float) (MathUtils.atan2(e.center().y-center().y, e.center().x-center().x)); }

    public Vector2 center() { return new Vector2(x + sprite.getWidth()/2, y + sprite.getHeight()/2); }

}
