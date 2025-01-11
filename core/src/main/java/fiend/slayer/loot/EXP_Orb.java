package fiend.slayer.loot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Player;
import fiend.slayer.screens.GameScreen;

public class EXP_Orb extends Entity{
    final GameScreen gs;
    Player plr;

    float speed = 0;
    float val;
    boolean func_running = false;

    Task moveToPlr;
    public EXP_Orb(final GameScreen gs,float mx,float my,float val) {
        super(gs);

        sprite = new Sprite(new Texture("loot/exp_orb.png"));
        autoSpriteSize();


        this.gs = gs;
        this.plr = gs.player;
        this.val = val;
        x=mx;y=my;

         moveToPlr = new Task(){
            @Override
            public void run() {
                speed += 0.01;
                float heading = getHeading(plr);
                x += Math.cos((double)heading) * speed;
                y += Math.sin((double)heading) * speed;

                if (collisionCheck(plr)) {
                    moveToPlr.cancel();
                    plr.energy = Math.min(plr.maxEnergy,plr.energy + val);
                    gs.exp_orbs.removeValue(EXP_Orb.this,false);
                }
            }
        };
    }


    @Override
    public void update(float delta) {
        if (plr.distanceToEntity(this) <= (plr.pickup_range / gs.tile_size)) {
            if (func_running == false) {
                func_running = true;
                Timer.schedule(moveToPlr,0f,delta);
            }
        }
    }
}
