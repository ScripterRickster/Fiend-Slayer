package fiend.slayer.loot;

import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Loot extends Entity{
    final GameScreen gs;

    public Loot(final GameScreen gs,float px,float py,String t1, String t2){
        super(gs);
        this.gs = gs;

        x = px; y = py;

        if(t1.equals("weapon")){
            sprite = new Sprite(new Texture("weapons/img/"+t2+".png"));
        }
    }
}
