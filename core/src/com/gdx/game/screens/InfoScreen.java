package com.gdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;

public class InfoScreen implements Screen {
    
    HuntersGame game;
    Texture background,backBefore,backAfter,textInfo;
    /**
     * Constructor, Links the Game instance
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     */
    public InfoScreen(HuntersGame game){
        this.game = game;
        //play button as image
        backBefore = new Texture("backBefore.png");
        backAfter = new Texture("backAfter.png");
        //text
        textInfo = new Texture("info.png");
        //background
        background = new Texture("grass-info.jpg");
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //positions of back button
        int xb= backBefore.getWidth();
        int yb= backBefore.getHeight();
        //positions of the mouse
        int x=Gdx.input.getX(),y=Gdx.input.getY();

        //start batch
        game.batch.begin();
        game.batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.draw(textInfo,0,-30,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //start script for touch and click by mouse

        if(x>=0 && x<=backBefore.getWidth() && y>=0 && y<=backBefore.getHeight()){
            game.batch.draw(backAfter,0,Gdx.graphics.getHeight()-backBefore.getHeight());
            if(Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new MenuScreen(game));
            }
        }else{
            game.batch.draw(backBefore,0,Gdx.graphics.getHeight()-backBefore.getHeight());
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
