package fiend.slayer.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Mob;
import fiend.slayer.entity.Player;
import fiend.slayer.screens.GameScreen;

public class Bullet extends Entity {

    double heading;

    public String type;
    public float speed = 10;

    public Entity source_entity;

    public Bullet(final FiendSlayer g, final GameScreen gs, Entity se, float heading) {
        super(g, gs,"bullet");

        source_entity = se;

        sprite = new Sprite(new Texture("bullet.png"));
        sprite.setSize(1, 1);

        x = source_entity.x; y = source_entity.y;
        this.heading = heading;

        sprite.setOrigin(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
        sprite.setRotation((float) Math.toDegrees(heading));

        //System.out.printf("Bullet X %f Y %f Source X %f Y %f\n", x, y, source_entity.x, source_entity.y);
    }

    @Override
    public void update(float delta) {

        x += (float) (speed * Math.cos(heading) * delta);
        y += (float) (speed * Math.sin(heading) * delta);

        if (gs.checkForCollisions(this)) {
            dead = true;
        }


        if ((x < 0 || x > 800 * 1/gs.tile_size) || (y < 0 || y > 600 * 1/gs.tile_size)){
            //System.out.println("DELETING...." + this.toString());

            this.dead = true;
        }

        if (!dead){
            if(source_entity instanceof Mob && this.collideWithOtherEntity(gs.player)){
                if(gs.player.armor == 0){
                    if(gs.player.hp <= 0){
                        gs.player.dead = true;

                    }else{
                        gs.player.hp--;
                    }
                }else{
                    gs.player.armor--;
                }
                System.out.println(gs.player);
                dead = true;

            }else if(source_entity instanceof Player){
                for(int i=gs.mobs.size-1;i>=0;--i){
                    if(this.collideWithOtherEntity(gs.mobs.get(i))){
                        Mob curr_mob = gs.mobs.get(i);
                        if(curr_mob.hp > 0){
                            curr_mob.hp--;
                        }else{
                            curr_mob.dead = true;
                        }
                        dead = true;
                        break;
                    }
                }
            }
        }

        if (this.collideWithOtherEntity(gs.player) && !dead){


        }


    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        if ((x < 0 || x > 800 * (1/gs.tile_size)) || (y < 0 || y > 600 * (1/gs.tile_size))){
            dead = true;
        }

        sprite.setPosition(x,y);
    }

    @Override
    public String toString(){ return "BULLET COORDS | X: " + x + " | Y: " + y + " | BULLET HEADING: " + heading; }
}
