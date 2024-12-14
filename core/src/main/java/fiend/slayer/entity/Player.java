package fiend.slayer.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import fiend.slayer.FiendSlayer;
import fiend.slayer.projectiles.Bullet;
import fiend.slayer.screens.GameScreen;

public class Player extends Entity {

    float speed = 8f;

    public int maxHP = 100;
    public int maxArmor = 10;
    public int maxEnergy = 200;

    public int hp = maxHP;
    public int armor = maxArmor;
    public int energy = maxEnergy;

    public boolean dead = false;

    private boolean headingLeft = false;

    Task regenArmor;

    public Player(final FiendSlayer g,final GameScreen gs) {
        super(g, gs,"player");

        sprite = new Sprite(new Texture("player.png"));
        sprite.setSize(1, 1);
        x = 0; y = 0;


        regenArmor = new Task(){
            @Override
            public void run(){
                if(dead == false && armor < maxArmor){
                    armor++;
                    System.out.println("Armor Increased");
                }
            }
        };
        Timer.schedule(regenArmor,0f,15f);



    }



    @Override
    public void update(float delta) {

        if(dead == false){
            float prevX = x;
            float prevY = y;

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                x += speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                x -= speed * delta;
                headingLeft = true;
            }else{
                headingLeft = false;
            }

            if (gs.checkForCollisions(this)) {
                x = prevX;
                y = prevY;
            }

            prevX = x;
            prevY = y;

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                y -= speed  * delta;
            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){

                float closestHeading = headingLeft == true ? (float) Math.toRadians(180.0) : 0f;
                float closestDist = Float.MAX_VALUE;
                for(int i=gs.mobs.size - 1; i>=0;--i){
                    Mob m = gs.mobs.get(i);

                    Vector2 mob_vec = new Vector2(m.x,m.y);
                    Vector2 plr_vec = new Vector2(x,y);
                    if(mob_vec.dst(plr_vec) < closestDist){
                        closestDist = mob_vec.dst(plr_vec);
                        closestHeading = this.getHeadingToOtherEntity(m);
                    }


                }
                Bullet b = new Bullet(game,gs,this,closestHeading);
                gs.bullets.add(b);
                if(energy > 0){
                    energy--;
                    System.out.println(this);
                }
            }

            if (gs.checkForCollisions(this)) {
                x = prevX;
                y = prevY;
            }

            sprite.setPosition(x, y);
        }else{
            regenArmor.cancel();
        }
    }



    @Override
    public String toString(){
        return "Player Info | CORDS -> X: " + x + " , Y: " + y + " ; Stats -> HP: " + hp + " -> Armor: " + armor + " -> Energy Remaining: " + energy;
    }

}
