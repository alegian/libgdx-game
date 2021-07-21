package com.gdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Handles all Economy related things
 */
public class Economy {
    /**
     * Drachma : Name of our Currency
     */
    private static int drachmas = 2000;
    /**
     * Adds some money to the player
     * @param income: the money to be added
     */
    public static void addMoney(int income){
        drachmas+=income;
    }
    /**
     * Removes some money from the player
     * @param cost: the money to be subtracted
     */
    public static boolean subtractMoney(int cost){
        if(drachmas-cost<0){
            return false;
        }
        drachmas-=cost;
        return true;
    }
    /**
     * Removes some money from the player. If the player does not have enough, they pay
     * as much as they have
     * @param cost: the money the player SHOULD pay
     */
    public static boolean hardSubtractMoney(int cost){
        if(drachmas-cost<0){
            drachmas=0;
            return false;
        }
        drachmas-=cost;
        return true;
    }
    public int getDrachmas(){
        return drachmas;
    }
    /**
     * Renders the money number on the top bar
     */
    public static void render(Batch batch, BitmapFont font){
        font.setColor(Color.BLACK);
        font.getData().setScale(1.2f);
        font.draw(batch, String.valueOf(drachmas), Gdx.graphics.getWidth()*0.87f, Gdx.graphics.getHeight()*0.98f,0,0,false);
    }
}
