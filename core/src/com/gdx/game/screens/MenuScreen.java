package com.gdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;

public class MenuScreen implements Screen {
    HuntersGame game;
    Texture background,playBefore,playAfter,exitBefore,exitAfter,infoAfter,infoBefore;

    public MenuScreen(HuntersGame game){
        this.game = game;
        //play button as image
        playBefore = new Texture("Startbefore.png");
        playAfter = new Texture("Startafter.png");
        //exit button as image
        exitBefore = new Texture("EXITbefore.png");
        exitAfter = new Texture("EXITafter.png");
        //info button as image
        infoAfter = new Texture("INFOafter.png");
        infoBefore = new Texture("INFObefore.png");
        //background
        background = new Texture("background.png");
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //positions of play button
        int xp=Gdx.graphics.getWidth()/2 - playBefore.getWidth()/2;
        int yp= playAfter.getHeight()+150;
        //positions of info button
        int xi=Gdx.graphics.getWidth()/2 - infoBefore.getWidth()/2;
        int yi= infoAfter.getHeight()+250;
        //positions of exit button
        int xe=Gdx.graphics.getWidth()/2 - exitBefore.getWidth()/2;
        int ye= exitAfter.getHeight()+350;
        //positions of the mouse
        int x=Gdx.input.getX(),y=Gdx.input.getY();
        //start batch
        game.batch.begin();
        game.batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //start script for touch and click by mouse
        if(x>xp && x<xp+playBefore.getWidth() && y>yp && y<yp+playBefore.getHeight()){
            game.batch.draw(playAfter,Gdx.graphics.getWidth()/2 -playBefore.getWidth()/2,400);
            if(Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new GameScreen(game));
                //System.out.println("start has been touched");
            }
        }else{
            game.batch.draw(playBefore,Gdx.graphics.getWidth()/2 -playBefore.getWidth()/2,400);
        }
        //info script for touch and click by mouse
        if(x>xi && x<xi+infoBefore.getWidth() && y>yi && y<yi+infoBefore.getHeight()){
            game.batch.draw(infoAfter,Gdx.graphics.getWidth()/2 -infoBefore.getWidth()/2,300);
            if(Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new InfoScreen(game));
                //System.out.println("info has been touched and clicked");
            }
        }else{
            game.batch.draw(infoBefore,Gdx.graphics.getWidth()/2 -infoBefore.getWidth()/2,300);
        }
        //exit script for touch and click by mouse
        if(x>xe && x<xe+exitBefore.getWidth() && y>ye && y<ye+exitBefore.getHeight()){
            game.batch.draw(exitAfter,Gdx.graphics.getWidth()/2 -exitBefore.getWidth()/2,200);
            if(Gdx.input.isTouched()){
                System.exit(0);
                //System.out.println("exit has been touched and clicked");
            }
        }else{
            game.batch.draw(exitBefore,Gdx.graphics.getWidth()/2 - exitBefore.getWidth()/2,200);
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
