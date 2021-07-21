package com.gdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Time {
    /**
     * day count
     */
    static int days=0;
    /**
     * Renders the money number on the top bar
     */
    public static void render(Batch batch, BitmapFont font, int frames){
        update(frames);
        font.setColor(Color.BLACK);
        font.draw(batch, "Day "+days, Gdx.graphics.getWidth()*0.78f, Gdx.graphics.getHeight()*0.98f,0,0,false);
    }
    /**
     * Converts frames to in game Days
     */
    private static void update(int frames){
        // 1 day = 50 sec
        int dayInFrames=60*50;
        // if a day has passed
        if(frames%dayInFrames==0){
            payCleaners();
            days++;
        }
    }
    /**
     * Pays the cleaners staff
     */
    private static void payCleaners(){
        // first calculate how many cleaners we have
        int cleanerCount = 0;
        for(IPlaceable p : World.placeables){
            if(p instanceof Cleaner) cleanerCount++;
        }
        // then pay them
        Economy.hardSubtractMoney(cleanerCount*10);
    }
}
