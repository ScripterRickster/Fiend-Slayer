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
    public float alpha = 1;

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
    public void setAlpha(float a) { alpha = a; sprite.setAlpha(alpha);}
    public float getAlpha() { return alpha;}

    public Rectangle getRectangle(){ return new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()); }

    public float distanceToEntity(Entity e){
        return Math.abs(new Vector2(this.x,this.y).dst(new Vector2(e.x,e.y)));
    }
    public boolean collisionCheck(Entity e){

        //System.out.println(e + " | " + e.getRectangle());
        //System.out.println(this + " | " + this.getRectangle());
        return this.getRectangle().overlaps(e.getRectangle());
    }

    public float getHeading(Entity e) { return (float) (MathUtils.atan2(e.center().y - center().y, e.center().x - center().x)); }

    public Vector2 center() { return new Vector2(x + sprite.getWidth()/2, y + sprite.getHeight()/2); }

}
