package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import fiend.slayer.FiendSlayer;

public class MainMenuScreen implements Screen {
    SpriteBatch batch;
    final FiendSlayer game;
    Button start_button;
    Stage stage;

    Texture bg; // bg = background
    TextureRegion tr; // for bg


    Texture start_norm;
    Texture start_hover;

    Music b_click;
    Music b_hover;

    boolean mouse_on_button = false;

    float tsx,tsy;


    public MainMenuScreen(final FiendSlayer g){
        stage = new Stage();
        batch = new SpriteBatch();
        game = g;
        bg = new Texture("StartMenuBackground.jpg");
        tr = new TextureRegion(bg);

        tsx = Gdx.graphics.getWidth(); tsy = Gdx.graphics.getHeight();

        b_click = Gdx.audio.newMusic(Gdx.files.internal("gui/sounds/b_click.mp3"));
        b_hover = Gdx.audio.newMusic(Gdx.files.internal("gui/sounds/b_hover.mp3"));

        start_norm = new Texture("start_norm.png");
        start_hover = new Texture("start_hov.png");

        Drawable normalDrawable = new TextureRegionDrawable(start_norm);
        Drawable hoverDrawable = new TextureRegionDrawable(start_hover);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = normalDrawable;
        buttonStyle.over = hoverDrawable;

        start_button = new Button(buttonStyle);
        start_button.setPosition(Gdx.graphics.getWidth() / 2f - start_button.getWidth() / 2f, Gdx.graphics.getHeight() / 3f - start_button.getHeight() / 4f);
        start_button.setTransform(true);
        start_button.setScaleX(game.ui_scale_x);
        start_button.setScaleY(game.ui_scale_y);
        start_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                b_click.play();
                b_click.setLooping(false);
                game.setScreen(new GameScreen(g));
                dispose();
            }
        });

        start_button.addListener(new InputListener() {
            public boolean mouseMoved(InputEvent evt, float ax, float ay){
                if(mouse_on_button == false){
                    mouse_on_button = true;
                    b_hover.play();
                    b_hover.setLooping(false);
                }
                return true;
            }
            public void exit(InputEvent evt, float ax, float ay,int pointer, Actor toActor){
                mouse_on_button = false;
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
        batch.draw(tr,0,0,tsx,tsy);
        batch.end();
        stage.act();
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        game.update_ui_scale(width, height);
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

        b_click.stop();
        b_hover.stop();
        b_click.dispose();
        b_hover.dispose();

    }
}
