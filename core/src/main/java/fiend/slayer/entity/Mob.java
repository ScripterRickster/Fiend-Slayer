package fiend.slayer.entity;

import java.util.Random;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import fiend.slayer.FiendSlayer;
import fiend.slayer.projectiles.Bullet;
import fiend.slayer.screens.GameScreen;

public class Mob {

    final FiendSlayer game;
    final GameScreen gs;

    public float x, y;

    public float speed = 1;

    public boolean sees_player = false;


    private boolean bullet_spawned = false;

    Sprite sprite;

    Random r = new Random();


    public Mob(final FiendSlayer g,final GameScreen gs,float tx,float ty){
        sprite = new Sprite(new Texture("mob1.png"));
        sprite.setSize(1, 1);

        game = g;
        this.gs = gs;
        x = tx; y = ty;

    }

    public Rectangle getRectangle(){
        return new Rectangle(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
    }

    public void update(float delta){
        float prevX = x;
        float prevY = y;

        int xDir = r.nextInt(2) - 1;
        int yDir = r.nextInt(2) - 1;

        if(xDir== 1){
            x += delta * speed;
        }else if(xDir == -1){
            x -= delta * speed;
        }


        if(yDir == 1){
            y += delta * speed;
        }else if(yDir == -1){
            y -= delta * speed;
        }

        if(!gs.checkForCollisions(x,y,this)){
            sprite.setPosition(x, y);
        }else{
            //System.out.println("Collided with objects");
            x = prevX;
            y = prevY;
        }

        Vector2 playerPosition = new Vector2(gs.player.x, gs.player.y);
        sees_player = checkLineOfSight(new Vector2(x, y), playerPosition);

        if(r.nextInt(100) <= 100){
            //if(gs.bullets.size == 0){bullet_spawned =false;}
            fire_projectile();
        }
        //System.out.println("Mob: " + this.toString() + " | Sees Player: " + sees_player);
    }

    private boolean checkLineOfSight(Vector2 start, Vector2 end) {
        MapLayer collisionLayer = gs.collisionLayer;

        if (collisionLayer == null) {
            return false;
        }

        for (RectangleMapObject object : collisionLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Rectangle scaled_rect = new Rectangle(rect.getX() * 1/gs.tile_size,rect.getY() * 1/gs.tile_size,rect.getWidth() * 1/gs.tile_size,rect.getHeight() * 1/gs.tile_size);
            if (Intersector.intersectSegmentRectangle(start, end, scaled_rect)) {
                return false;
            }
        }

        return true;
    }

    public double getHeadingToPlayer(){
        float dx = gs.player.x - x;
        float dy = gs.player.y - y;

        double deg = Math.toDegrees(Math.atan2(dy,dx));

        deg += deg < 0 ? 360 : 0;

        return deg;
    }

    private void fire_projectile(){
        if(sees_player && bullet_spawned == false){
            Bullet b = new Bullet(game,gs,x,y,getHeadingToPlayer(),"mob");
            System.out.println(this.toString() + "\n" + b.toString());
            gs.addObject(b);
            //bullet_spawned = true;
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public String toString(){
        return "MOB CORDS | X: " + x + " | Y: " + y;
    }
}
