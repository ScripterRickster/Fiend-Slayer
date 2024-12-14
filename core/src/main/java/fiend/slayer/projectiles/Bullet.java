package fiend.slayer.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;

public class Bullet extends Entity {

    double heading;

    public String type;
    public float speed = 10;

    public Bullet(final FiendSlayer g, final GameScreen gs, Entity source_entity, double heading, String type){
        super(g, gs);

        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(1, 1);

        x = source_entity.x; y = source_entity.y;

        this.heading = heading;

        sprite.rotate((float)heading);
        this.type = type;
    }

    @Override
    public void update(float delta){

        x += (float)(speed*delta*Math.cos(Math.toRadians(heading)));
        y += (float)(speed*delta*Math.sin(Math.toRadians(heading)));
        sprite.setPosition(x,y);

        System.out.println(this.toString());

        //System.out.println(x + " | " + y);

        if ((x < 0 || x > 800 * (1/gs.tile_size)) || (y < 0 || y > 600 * (1/gs.tile_size))){
            // System.out.println("DELETING...." + this.toString());
            dead = true;
        }
    }

    @Override
    public String toString(){
        return "BULLET COORDS | X: " + x + " | Y: " + y + " | BULLET HEADING: " + heading;
    }
}
