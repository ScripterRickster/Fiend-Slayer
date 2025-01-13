package fiend.slayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;


import fiend.slayer.screens.MainScreen;

public class FiendSlayer extends Game {
    public float ui_scale_x = 1;
    public float ui_scale_y = 1;
    public float osx,osy;
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.setScreen(new MainScreen(this,true));
        osx = Gdx.graphics.getWidth();
        osy = Gdx.graphics.getHeight();
    }

    public void update_ui_scale(float width, float height){
        /*
        ui_scale_x = width/osx;
        ui_scale_y = height/osy;
        */
        float uniform_scale = Math.min(width/osx, height/osy);
        ui_scale_x = uniform_scale;
        ui_scale_y = uniform_scale;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
