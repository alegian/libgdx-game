package com.gdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.screens.GameScreen;
import com.gdx.game.screens.MenuScreen;


public class HuntersGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen((this)));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
