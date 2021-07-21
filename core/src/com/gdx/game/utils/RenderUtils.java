package com.gdx.game.utils;

import com.badlogic.gdx.Gdx;

public class RenderUtils {

    //calculate grid coordinates from mouse X, Y
    public static int[] getPositionFromMouse(){
        int[] pos = {0,0};
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight()-Gdx.input.getY();
        final int height=50;
        final int width=100;
        final float approxX = mouseY/height + mouseX/width-1/2f;
        final float approxY = mouseY/height - mouseX/width+1/2f;

        pos[0] = (int)Math.floor(approxX);
        pos[1] = (int)Math.floor(approxY);

        return pos;
    }
}
