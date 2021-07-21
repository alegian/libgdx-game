package com.gdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter ;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.HuntersGame;
import com.gdx.game.entities.UserInterface;
import com.gdx.game.entities.World;

/**
 * In-Game Screen
 */
public class GameScreen extends ScreenAdapter  {

    HuntersGame game;
    ShapeRenderer shapeRenderer;
    Texture grassImg;
    UserInterface ui;
    World world;
    /**
     * Constructor, Links the Game instance
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     */
    public GameScreen (HuntersGame game){
        this.game = game;
    }
    /**
     * Screen-Overridden method: Executes when this Screen is shown
     */
    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        grassImg = new Texture("grass.png");
        world = new World(game, shapeRenderer);
        ui = new UserInterface(game, world);
        ui.initTextures();

        //Input handler, should register all click handlers here
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                ui.onClick(x,y);
                return true;
            }
        });
    }
    /**
     * Render-Helper function: draws the grass Background
     */
    private void drawBackground(){
        game.batch.begin();
        game.batch.draw(grassImg, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
    }
    /**
     * Screen-Overridden method: Renders, executes once every frame is drawn. Runs the individual
     * render functions from everything (World, UI)
     */
    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();
        world.render(delta);
        ui.render(delta);
    }
    /**
     * Screen-Overridden method: Runs when the screen changes to some other
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    /**
     * Memory Cleanup function that triggers the individual cleanups
     */
    @Override
    public void dispose() {
        grassImg.dispose();
        ui.dispose();
        world.dispose();
    }
}
