package com.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.game.HuntersGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//you can get a list of things you can change and a lot of OpenGL setting

		config.foregroundFPS = 60;
		config.title = "Hunters";
		config.width = 1260;
		config.height = 700;
		new LwjglApplication(new HuntersGame(), config);
	}
}
