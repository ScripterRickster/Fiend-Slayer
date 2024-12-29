package fiend.slayer.loot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import fiend.slayer.FiendSlayer;
import fiend.slayer.entity.Entity;
import fiend.slayer.entity.Player;
import fiend.slayer.screens.GameScreen;
import com.badlogic.gdx.utils.Timer.Task;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Chest extends Entity{
    final GameScreen gs;

    String reward;
    Random rng;
    Player plr;
    boolean fadeout = false;
    Task fade;
    public Chest(final GameScreen gs,float px, float py,Player plr){
        super(gs);
        this.gs = gs;
        this.plr = plr;

        sprite = new Sprite(new Texture("assets/loot/chest_texture.png"));
        autoSpriteSize();
        x = px; y = py;

        rng = new Random();

        if(rng.nextInt(1)+1 == 1){ // weapon or potion
            reward = "weapon";
        }else{
            reward = "potion";
        }

        fade = new Task(){
            @Override
            public void run(){
                if(fadeout){
                  setAlpha(getAlpha()+0.05f);
                }
            }
        };
        Timer.schedule(fade,0f,0.25f);

    }

    public void spawnReward(){
        if(reward.equals("weapon")){
            String wpn = "";

            Path folderPath = Paths.get("weapons/configs");

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
                List<Path> pathList = new ArrayList<>();
                stream.forEach(pathList::add);

                if(!pathList.isEmpty()){
                    int random_idx = rng.nextInt(pathList.size());
                    wpn = pathList.get(random_idx).getFileName().toString();
                    wpn = wpn.substring(0,wpn.length()-6);
                    System.out.println(wpn);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(float delta){
        if(fadeout == false && collisionCheck(plr)){
            fadeout = true;
            spawnReward();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }



}
