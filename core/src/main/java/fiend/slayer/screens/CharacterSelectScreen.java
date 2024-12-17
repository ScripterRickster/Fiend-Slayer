package fiend.slayer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObjects;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
//import com.badlogic.gdx.utils.viewport.FitViewport;

import fiend.slayer.FiendSlayer;
//import fiend.slayer.entity.Player;

public class CharacterSelectScreen implements Screen {
    final FiendSlayer game;

    //SpriteBatch batch;
    Stage stage;
    public float tile_size;
    public TiledMap tiledmap;
    public OrthogonalTiledMapRenderer tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / 16f);
    public ExtendViewport viewport;

    MapProperties map_prop;
    public int map_x,map_y;

    public BitmapFont font;
    //Array<Button> char_buttons;




    public CharacterSelectScreen(final FiendSlayer g) {
        this.game = g;

        //char_buttons = new Array<>();

        stage = new Stage();

        font = new BitmapFont();
        tiledmap = new TmxMapLoader().load("character_selection.tmx");
        map_prop = tiledmap.getProperties();

        MapObjects objects = tiledmap.getLayers().get("characters").getObjects();

        map_x = map_prop.get("width",Integer.class);
        map_y = map_prop.get("height",Integer.class);



        tile_size = map_prop.get("tilewidth", Integer.class);
        tiledmap_renderer = new OrthogonalTiledMapRenderer(tiledmap, 1 / tile_size);
        viewport = new ExtendViewport(16, 16);


        for(RectangleMapObject r: objects.getByType(RectangleMapObject.class)){
            String c_name = r.getName();
            int armor = r.getProperties().get("armor",Integer.class);
            int hp = r.getProperties().get("hp",Integer.class);
            int energy = r.getProperties().get("energy",Integer.class);

            String fp_norm = "char_images/"+c_name+".png";
            String fp_hover = "char_images/"+c_name+"h.png";



            Texture char_norm = new Texture(fp_norm);
            Texture char_hover = new Texture(fp_hover);



            float cx = char_norm.getWidth(); float cy = char_norm.getHeight();

            Drawable normalDrawable = new TextureRegionDrawable(char_norm);
            Drawable hoverDrawable = new TextureRegionDrawable(char_hover);

            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = normalDrawable;
            buttonStyle.over = hoverDrawable;

            Button char_select_button = new Button(buttonStyle);

            char_select_button = new Button(buttonStyle);
            float rc_x = r.getRectangle().getX();
            float rc_y = r.getRectangle().getY();

            System.out.println(rc_x);
            System.out.println(rc_y);
            char_select_button.setPosition(rc_x,rc_y);
            //System.out.println("X: " + char_select_button.getX() + " | Y: " + char_select_button.getY());

            //char_buttons.add(char_select_button);


            char_select_button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game));
                    dispose();
                }
            });

            stage.addActor(char_select_button);
            Gdx.input.setInputProcessor(stage);

        }


    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        viewport.getCamera().position.set(map_x/2,map_y/2,0);
        tiledmap_renderer.setView((OrthographicCamera) viewport.getCamera());
        tiledmap_renderer.render();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            System.out.println("hi");
        }
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        //stage.getViewport().update(width, height, true);
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
    public void dispose() { // this is not called automatically
        stage.dispose();
    }
}
