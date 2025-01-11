package fiend.slayer;

import com.badlogic.gdx.Game;

import fiend.slayer.screens.MainMenuScreen;

public class FiendSlayer extends Game {
    @Override
    public void create() {
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
