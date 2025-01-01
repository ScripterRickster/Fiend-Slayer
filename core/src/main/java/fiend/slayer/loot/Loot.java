package fiend.slayer.loot;

import fiend.slayer.entity.Entity;
import fiend.slayer.screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Loot extends Entity{
    final GameScreen gs;
    public final String r_class,r_type;

    public Loot(final GameScreen gs,float px,float py,final String t1, final String t2){
        super(gs);
        this.gs = gs;

        r_class = t1;
        r_type = t2;

        x = px; y = py;

        if(t1.equals("weapon")){
            sprite = new Sprite(new Texture("weapons/img/"+t2+".png"));
            sprite.setSize(sprite.getWidth() / gs.tile_size, sprite.getHeight() / gs.tile_size);
        }else if(t1.equals("potion")){
            sprite = new Sprite(new Texture("loot/"+t2+".png"));
            sprite.setSize(sprite.getWidth() / gs.tile_size, sprite.getHeight() / gs.tile_size);
        }
    }


}
