package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import fiend.slayer.FiendSlayer;

public class EndScreen implements Screen {


    public BitmapFont font;
    public FiendSlayer game;
    SpriteBatch batch;
    Button start_button;
    Button exit_button;
    Stage stage;

    Texture bg; // bg = background
    TextureRegion tr; // for bg

    Texture start_norm;
    Texture start_hover;

    Texture exit_norm;
    Texture exit_hover;

    float sx, sy;

    public EndScreen(FiendSlayer g) {
        stage = new Stage();
        batch = new SpriteBatch();
        font = new BitmapFont();
        game = g;
        bg = new Texture("ui/StartMenuBackground.jpg");
        tr = new TextureRegion(bg);

        sx = Gdx.graphics.getWidth();
        sy = Gdx.graphics.getHeight();

        start_norm = new Texture("ui/restart_norm.png");
        start_hover = new Texture("ui/restart_hov.png");

        exit_norm = new Texture("ui/exit_norm.png");
        exit_hover = new Texture("ui/exit_hov.png");

        Drawable start_normalDrawable = new TextureRegionDrawable(start_norm);
        Drawable start_hoverDrawable = new TextureRegionDrawable(start_hover);

        Drawable exit_normalDrawable = new TextureRegionDrawable(exit_norm);
        Drawable exit_hoverDrawable = new TextureRegionDrawable(exit_hover);

        Button.ButtonStyle start_buttonStyle = new Button.ButtonStyle();
        start_buttonStyle.up = start_normalDrawable;
        start_buttonStyle.over = start_hoverDrawable;

        start_button = new Button(start_buttonStyle);
        start_button.setPosition(Gdx.graphics.getWidth() / 2f - start_button.getWidth() / 2f, Gdx.graphics.getHeight() / 3f - start_button.getHeight() / 4f);

        Button.ButtonStyle exit_buttonStyle = new Button.ButtonStyle();
        exit_buttonStyle.up = exit_normalDrawable;
        exit_buttonStyle.over = exit_hoverDrawable;
        exit_button = new Button(exit_buttonStyle);
        exit_button.setPosition(Gdx.graphics.getWidth() / 2f - start_button.getWidth() / 2f, Gdx.graphics.getHeight() / 4f - start_button.getHeight() / 2f);

        start_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        exit_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        stage.addActor(start_button);
        stage.addActor(exit_button);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        batch.draw(tr, 0, 0, sx, sy);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        bg.dispose();
        start_norm.dispose();
        start_hover.dispose();
    }
}
