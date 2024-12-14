package fiend.slayer.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Bullet {

    final FiendSlayer game;
    final GameScreen gs;

    public float x, y;

    public float speed = 10;

    double heading;

    Sprite sprite;

    public String type;

    public Bullet(final FiendSlayer g,final GameScreen gs,float tx,float ty,double heading,String type){
        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(1, 1);

        game = g;
        this.gs = gs;
        x = tx; y = ty;

        this.heading = heading;

        sprite.setRotation((float)heading);
        this.type = type;
    }

    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }

    public void update(float delta){
        x += (float)(speed*delta*Math.cos(Math.toRadians(heading)));
        y += (float)(speed*delta*Math.sin(Math.toRadians(heading)));
        sprite.setPosition(x,y);

        System.out.println(this.toString());

        //System.out.println(x + " | " + y);

        /*if(gs.checkForCollisions(x, y, this)){
            gs.removeObject(this);
        }*/

        if((x < 0 || x > 800 * 1/gs.tile_size) || (y < 0 || y > 600 * 1/gs.tile_size)){
            System.out.println("DELETING...." + this.toString());

            gs.removeObject(this);
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public String toString(){
        return "BULLET CORDS | X: " + x + " | Y: " + y + " | BULLET HEADING: " + heading;
    }
}
