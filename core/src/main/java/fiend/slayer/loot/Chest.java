package fiend.slayer.loot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;

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
    Music open_sfx;
    public Chest(final GameScreen gs,float px, float py,Player plr){
        super(gs);
        x = px; y = py;
        sprite = new Sprite(new Texture("loot/chest_texture.png"));
        sprite.setPosition(x,y);
        autoSpriteSize();
        this.gs = gs;
        this.plr = plr;

        open_sfx = Gdx.audio.newMusic(Gdx.files.internal("loot/sounds/chest_open.mp3"));

        rng = new Random();

        if(rng.nextInt(2)+1 == 1){ // weapon or potion
            reward = "weapon";
        }else{
            reward = "potion";
        }

        fade = new Task(){
            @Override
            public void run(){
                if(fadeout){
                  setAlpha(getAlpha()-0.05f);
                }
            }
        };


    }

    public void playSound(){
        open_sfx.play();
        open_sfx.setLooping(false);
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
                    wpn = wpn.substring(0,wpn.length()-5);
                    gs.loot.add(new Loot(gs,x,y,reward,wpn));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reward.equals("potion")){
            if(rng.nextInt(2) == 1){
                gs.loot.add(new Loot(gs,x,y,reward,"hp_potion"));
            }else{
                gs.loot.add(new Loot(gs,x,y,reward,"energy_potion"));
            }
        }
    }

    @Override
    public void update(float delta){
        if(fadeout == false && collisionCheck(plr)){
            fadeout = true;
            Timer.schedule(fade,0f,0.1f);
            spawnReward();
            playSound();
        }
    }

    public void disposeSounds(){

        open_sfx.stop();
        open_sfx.dispose();

    }



}
