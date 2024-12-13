package fiend.slayer.entity;

import java.util.Random;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Mob {

    final FiendSlayer game;
    final GameScreen gs;

    public float x, y;

    public float speed = 1;

    public boolean sees_player = false;

    Random r = new Random();

    Sprite sprite;
    public Mob(final FiendSlayer g,final GameScreen gs,float tx,float ty){
        sprite = new Sprite(new Texture("mob1.png"));
        sprite.setSize(1, 1);

        game = g;
        this.gs = gs;
        x = tx; y = ty;

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

        if(!gs.checkForCollisions(x,y)){
            sprite.setPosition(x, y);
        }else{
            //System.out.println("Collided with objects");
            x = prevX;
            y = prevY;
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
