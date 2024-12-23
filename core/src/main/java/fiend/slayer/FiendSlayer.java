package fiend.slayer;

import com.badlogic.gdx.Game;
import fiend.slayer.screens.GameScreen;

public class FiendSlayer extends Game {
    @Override
    public void create() {
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
