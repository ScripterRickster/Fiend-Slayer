package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import fiend.slayer.FiendSlayer;
import fiend.slayer.screens.GameScreen;

public class Player {
    final FiendSlayer game;
    final GameScreen gs;

    final private Sprite sprite;
    public float x, y;

    public Player(final FiendSlayer g,final GameScreen gs) {
        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);

        game = g;
        this.gs = gs;
        x = 0; y = 0;
    }

    public Rectangle getRectangle(){
        return new Rectangle(x,y,sprite.getWidth(),sprite.getHeight());
    }

    public void update(float delta) {

        float prevX = x;
        float prevY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += delta * 4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= delta * 4f;
        }

        if(!gs.checkForCollisions()){
            sprite.setPosition(x, y);
        }else{
            System.out.println("Collided with objects");
            x = prevX;
            y = prevY;
        }


    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
