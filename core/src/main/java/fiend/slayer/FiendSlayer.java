package fiend.slayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import fiend.slayer.screens.MainMenuScreen;

public class FiendSlayer extends Game {
    public float ui_scale_factor = 1;
    public float osx,osy;
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        this.setScreen(new MainMenuScreen(this));
        osx = Gdx.graphics.getWidth();
        osy = Gdx.graphics.getHeight();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
