package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import fiend.slayer.FiendSlayer;

public class EndScreen implements Screen{


    SpriteBatch batch;
    final FiendSlayer game;
    Button start_button;
    Button exit_button;
    Stage stage;

    Texture bg; // bg = background
    TextureRegion tr; // for bg


    Texture start_norm;
    Texture start_hover;

    Texture exit_norm;
    Texture exit_hover;

    Music b_click;
    Music b_hover;

    boolean mouse_on_sbutton = false;
    boolean mouse_on_ebutton = false;

    float tsx,tsy;

    public EndScreen(final FiendSlayer g){
        stage = new Stage();
        batch = new SpriteBatch();
        game = g;
        bg = new Texture("StartMenuBackground.jpg");
        tr = new TextureRegion(bg);
        tsx = Gdx.graphics.getWidth(); tsy = Gdx.graphics.getHeight();

        b_click = Gdx.audio.newMusic(Gdx.files.internal("gui/sounds/b_click.mp3"));
        b_hover = Gdx.audio.newMusic(Gdx.files.internal("gui/sounds/b_hover.mp3"));

        start_norm = new Texture("restart_norm.png");
        start_hover = new Texture("restart_hov.png");

        exit_norm = new Texture("exit_norm.png");
        exit_hover = new Texture("exit_hov.png");

        Drawable start_normalDrawable = new TextureRegionDrawable(start_norm);
        Drawable start_hoverDrawable = new TextureRegionDrawable(start_hover);

        Drawable exit_normalDrawable = new TextureRegionDrawable(exit_norm);
        Drawable exit_hoverDrawable = new TextureRegionDrawable(exit_hover);

        Button.ButtonStyle start_buttonStyle = new Button.ButtonStyle();
        start_buttonStyle.up = start_normalDrawable;
        start_buttonStyle.over = start_hoverDrawable;

        start_button = new Button(start_buttonStyle);
        start_button.setTransform(true);
        start_button.setScale(game.ui_scale_x,game.ui_scale_y);
        start_button.setPosition(Gdx.graphics.getWidth() / 2f - start_button.getWidth() / 2f, Gdx.graphics.getHeight() / 3f - start_button.getHeight() / 4f);

        Button.ButtonStyle exit_buttonStyle = new Button.ButtonStyle();
        exit_buttonStyle.up = exit_normalDrawable;
        exit_buttonStyle.over = exit_hoverDrawable;
        exit_button = new Button(exit_buttonStyle);

        exit_button.setTransform(true);
        exit_button.setScale(game.ui_scale_x,game.ui_scale_y);
        exit_button.setPosition(Gdx.graphics.getWidth() / (2f*game.ui_scale_x) - exit_button.getWidth() / 2f, Gdx.graphics.getHeight() / 4f - exit_button.getHeight() / 2f);

        start_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try{
                    b_click.play();
                    b_click.setLooping(false);

                    game.setScreen(new GameScreen(game));
                    dispose();

                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });

        start_button.addListener(new InputListener() {
            public boolean mouseMoved(InputEvent evt, float ax, float ay){
                if(mouse_on_sbutton == false){
                    mouse_on_sbutton = true;
                    b_hover.play();
                    b_hover.setLooping(false);
                }
                return true;
            }
            public void exit(InputEvent evt, float ax, float ay,int pointer, Actor toActor){
                mouse_on_sbutton = false;
            }
        });

        exit_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        exit_button.addListener(new InputListener() {
            public boolean mouseMoved(InputEvent evt, float ax, float ay){
                if(mouse_on_ebutton == false){
                    mouse_on_ebutton = true;
                    b_hover.play();
                    b_hover.setLooping(false);
                }
                return true;
            }
            public void exit(InputEvent evt, float ax, float ay,int pointer, Actor toActor){
                mouse_on_ebutton = false;
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
        start_norm.dispose();
        start_hover.dispose();


        b_click.stop();
        b_hover.stop();
        b_click.dispose();
        b_hover.dispose();

    }
}
