package com.gdx.game.entities;

import com.gdx.game.utils.Distance;

import java.util.ArrayList;


public interface IPlaceable {
    int getCost();
    ArrayList<Distance> getOverlappingTiles();
    int[] getPlacementPos();
    void render(float delta);
    PlaceableType getType();
}
