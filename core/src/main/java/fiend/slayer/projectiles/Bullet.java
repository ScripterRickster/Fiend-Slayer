package fiend.slayer.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< HEAD
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

        //sprite.setPosition(tx,ty);

        game = g;
        this.gs = gs;
        x = tx; y = ty;
=======

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;

public class Bullet extends Entity {

    Entity source_entity;

    double heading;

    public String type;
    public float speed = 10;

    public Bullet(final FiendSlayer g, final GameScreen gs, Entity source_entity, double heading, String type){
        super(g, gs);

        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(1, 1);

        x = source_entity.x; y = source_entity.y;
>>>>>>> 2e237b02409830aca85b68ca885a1df9c094c066

        this.heading = heading;

        sprite.rotate((float)heading);
        this.type = type;


    }

<<<<<<< HEAD
    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }

    public void update(float delta){
=======
    @Override
    public void update(float delta){

>>>>>>> 2e237b02409830aca85b68ca885a1df9c094c066
        x += (float)(speed*delta*Math.cos(Math.toRadians(heading)));
        y += (float)(speed*delta*Math.sin(Math.toRadians(heading)));
        sprite.setPosition(x,y);

        System.out.println(this.toString());

        //System.out.println(x + " | " + y);

        /*if(gs.checkForCollisions(x, y, this)){
            gs.removeObject(this);
        }*/

<<<<<<< HEAD
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
=======
        if ((x < 0 || x > 800 * (1/gs.tile_size)) || (y < 0 || y > 600 * (1/gs.tile_size))){
            // System.out.println("DELETING...." + this.toString());
            dead = true;
        }
    }

    @Override
    public String toString(){
        return "BULLET COORDS | X: " + x + " | Y: " + y + " | BULLET HEADING: " + heading;
>>>>>>> 2e237b02409830aca85b68ca885a1df9c094c066
    }
}
