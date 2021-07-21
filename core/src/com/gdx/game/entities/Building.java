package com.gdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;
import com.gdx.game.utils.Distance;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/**
 * Represents a Building in the World
 */
public class Building implements IPlaceable{
    static Random random = new Random();
    Texture texture;
    Texture brokenTexture;
    HuntersGame game;
    PlaceableType type;
    /**
     * 2D position vector in Tile Coordinates
     */
    int[] pos;
    /**
     * timer variable
     */
    int frameCounter = 0;
    /**
     * on which frame the building should break
     */
    int breakFrame = -1;
    /**
     * if building is broken
     */
    boolean isBroken = false;
    /**
     * if building is being repaired
     */
    boolean beingRepaired = false;
    /**
     * Queue of guests
     */
    Queue<Guest> guestQueue = new LinkedList<>();
    /**
     * List of occupied squares that the building requires to be placed
     */
    final ArrayList<Distance> overlappingTiles;
    /**
     * Constructor, Links the Game instance and initializes the position
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param type Enumeration Building Type
     * @param x the building's horizontal position in Tile Coordinates
     * @param y the building's vertical position in Tile Coordinates
     */
    public Building (HuntersGame game, PlaceableType type, int x, int y) {
 try {
     this.game = game;
     this.type = type;
     brokenTexture = new Texture("repairman.png");

 }catch(Exception ex){}
        pos = new int[]{x, y};

        ArrayList<Distance> tiles = new ArrayList<>();
        switch (type) {
            case ROLLERCOASTER:
                tiles.add(new Distance(0, -1));
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, -1));
                tiles.add(new Distance(1, 1));
                tiles.add(new Distance(2, 0));
                tiles.add(new Distance(2, -1));
                tiles.add(new Distance(2, 1));
                break;
            case BUMPER_CARS:
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(0, 2));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, 1));
                tiles.add(new Distance(1, 2));
                break;
            case CASTLE:
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, 1));
                break;
            case CAROUSEL:
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, 1));
                break;
            case SHIP:
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(2, 0));
                break;
            case FOUNTAIN:
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, 1));
                break;
            case CAFE:
                tiles.add(new Distance(0, 1));
                tiles.add(new Distance(1, 0));
                tiles.add(new Distance(1, 1));
                break;
        }
        this.overlappingTiles = tiles;

    }

    /**
     * Creates Texture Instance
     */
    public void initTexture(){
        texture = new Texture(type.getTextureName());
    }
    /**
     * Main method, Renders the building at the right location of the screen, depending on
     * the pos[] vector. ASSUMES BATCH STARTS AND ENDS PROPERLY
     */
    @Override
    public void render (float delta) {
        final float x = (pos[0] - pos[1]) * 100 / 2f;
        final float y = (pos[0] + pos[1]) * 50 / 2f;
        switch (type){
            case ROLLERCOASTER:
                game.batch.draw(texture, x, y, 200f, 200f);
                break;
            case BUMPER_CARS:
                game.batch.draw(texture, x-75, y-35, 200f, 200f);
                break;
            case CASTLE:
                game.batch.draw(texture, x-50, y, 200f, 150f);
                break;
            case CAROUSEL:
                game.batch.draw(texture, x-10, y+17, 125f, 125f);
                break;
            case PENDULUM:
                game.batch.draw(texture, x-47, y-4, 200f, 200f);
                break;
            case SHIP:
                game.batch.draw(texture, x+6, y+3, 180f, 165f);
                break;
            case DROP_TOWER:
                game.batch.draw(texture, x-50, y+3, 200f, 200f);
                break;
            case AIM:
                game.batch.draw(texture, x, y, 100f, 100f);
                break;
            case TRASHCAN:
                game.batch.draw(texture, x+2, y-12, 100f, 100f);
                break;
            case TREE:
                game.batch.draw(texture, x, y, 100f, 100f);
                break;
            case PATH:
                game.batch.draw(texture, x, y, 100f, 50f);
                break;
            case FOUNTAIN:
                game.batch.draw(texture, x-35, y-5, 180f, 135f);
                break;
            case CAFE:
                game.batch.draw(texture, x-60, y, 190f, 190f);
                break;
            case STAND1:
                game.batch.draw(texture, x+10, y+10, 75f, 75f);
                break;
            case STAND2:
                game.batch.draw(texture, x+10, y+10, 75f, 75f);
                break;
            case STAND3:
                game.batch.draw(texture, x+10, y+10, 75f, 75f);
                break;
            case STAND4:
                game.batch.draw(texture, x+10, y+10, 75f, 75f);
                break;
        }
        // draw the hammer icon only if broken
        if(isBroken) game.batch.draw(brokenTexture, x+20, y+50, 75f, 75f);
        update();
    }
    /**
     * handles every frame calculations
     */
    public void update(){
        if(!isBroken) {
            // guest handling
            if (guestQueue.size() > 0) {
                Guest curr = guestQueue.peek();
                if (curr != null && curr.getMode() == GuestMode.WAITING) {
                    curr.setMode(GuestMode.USING);
                }
            }
        }
        // every 10 minutes choose a break frame and reset frame counter
        if(frameCounter%(60*60*10)==0){
            frameCounter=0;
            breakFrame = random.nextInt(60*60*10);
        }
        // if we reach the break frame, then break
        if(frameCounter==breakFrame && type!= PlaceableType.TREE && type!= PlaceableType.PATH && type!= PlaceableType.FOUNTAIN && type!= PlaceableType.TRASHCAN){
            isBroken=true;
        }
        frameCounter++;
    }
    /**
     * Returns the Buildings creation cost
     */
    public int getCost () {
        return type.getCost();
    }
    /**
     * Returns the Buildings entry fee
     */
    public int getTicketCost () {
        return type.getTicketCost();
    }
    /**
     * Returns whether guests using this building should disappear
     */
    public boolean usersVanish(){
        switch (type){
            case ROLLERCOASTER:
            case BUMPER_CARS:
            case CASTLE:
            case CAROUSEL:
            case PENDULUM:
            case SHIP:
            case DROP_TOWER:
            case CAFE:
                return true;
        }
        return false;
    }
    /**
     * Memory Cleanup function that is executed in the Screen's respective function
     */
    public void dispose() {
        texture.dispose();
    }
    /**
     * Adds a new Guest to the waiting queue
     */
    public boolean queueGuest(Guest g){
        return guestQueue.add(g);
    }
    /**
     * Removes a Guest from the HEAD of the waiting queue
     */
    public void dequeueGuest(){
        guestQueue.remove();
    }
    /**
     * Removes a Guest from any position in the waiting queue
     */
    public void removeGuest(Guest g){
        guestQueue.remove(g);
    }
    /**
     * Reset the building's waiting queue
     */
    public void resetGuestQueue(){
        guestQueue.clear();
    }
    /**
     * Getters / Setters
     */
    public int[] getPos(){
        return pos;
    }
    /*public void setPos(int x, int y){
        pos[0] = x;
        pos[1] = y;
    }*/
    public boolean getBroken(){
        return isBroken;
    }
    public void setBroken(boolean isBroken){
        this.isBroken = isBroken;
    }
    public boolean getBeingRepaired(){
        return beingRepaired;
    }
    public void setBeingRepaired(boolean beingRepaired){
        this.beingRepaired = beingRepaired;
    }
    public PlaceableType getType(){
        return type;
    }
    public ArrayList<Distance> getOverlappingTiles(){
        return overlappingTiles;
    }

    @Override
    public int[] getPlacementPos() {
        return getPos();
    }
}
