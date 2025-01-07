package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fiend.slayer.FiendSlayer;

public class MainMenuScreen implements Screen {
    SpriteBatch batch;
    public BitmapFont font;
    public FiendSlayer game;
    Button start_button;
    Stage stage;

    Texture bg; // bg = background
    TextureRegion tr; // for bg


    Texture start_norm;
    Texture start_hover;

    float sx,sy;
    public MainMenuScreen(FiendSlayer g){
        stage = new Stage();
        batch = new SpriteBatch();
        font = new BitmapFont();
        game = g;
        bg = new Texture("StartMenuBackground.jpg");
        tr = new TextureRegion(bg);

        sx = Gdx.graphics.getWidth(); sy = Gdx.graphics.getHeight();

        start_norm = new Texture("start_norm.png");
        start_hover = new Texture("start_hov.png");

        Drawable normalDrawable = new TextureRegionDrawable(start_norm);
        Drawable hoverDrawable = new TextureRegionDrawable(start_hover);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = normalDrawable;
        buttonStyle.over = hoverDrawable;

        start_button = new Button(buttonStyle);
        start_button.setPosition(Gdx.graphics.getWidth() / 2f - start_button.getWidth() / 2f, Gdx.graphics.getHeight() / 3f - start_button.getHeight() / 4f);

        start_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                //game.setScreen(new CharacterSelectScreen(game));
                dispose();
            }
        });

        stage.addActor(start_button);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        batch.draw(tr,0,0,sx,sy);
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
