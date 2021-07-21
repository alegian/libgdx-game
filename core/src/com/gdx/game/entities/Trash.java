package com.gdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;
import java.util.Random;

public class Trash {
    /**
     * Static Texture
     */
    static Texture texture1 ;
    static Texture texture2 ;
    static Texture texture3 ;
    static Texture texture4 ;
    static Texture texture5 ;
    Texture texture;
    static Random random = new Random();
    HuntersGame game;
    World world;
    /**
     * 2D position vector in interpolated Tile Coordinates
     */
    float[] pos;
    /**
     * if some cleaner is coming to this trash
     */
    boolean isBeingCollected = false;
    /**
     * Constructor, Links the World and Game instances and initializes the position
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param world the World instance to access placed buildings etc
     * @param x the building's horizontal position in interpolated Tile Coordinates
     * @param y the building's vertical position in interpolated Tile Coordinates
     */
    public Trash (HuntersGame game, World world, int x, int y){
        try {
            this.game = game;
            this.world = world;
            texture1 = new Texture("trash1.png");
            texture2 = new Texture("trash2.png");
            texture3 = new Texture("trash3.png");
            texture4 = new Texture("trash4.png");
            texture5 = new Texture("trash5.png");

            float offsetX = random.nextFloat() / 4;
            float offsetY = random.nextFloat() / 4;
            pos = new float[]{x * 1f + offsetX, y * 1f + offsetY};
            // pick a random texture
            int textureIndex = random.nextInt(5);
            switch (textureIndex) {
                case 0:
                    texture = texture1;
                    break;
                case 1:
                    texture = texture2;
                    break;
                case 2:
                    texture = texture3;
                    break;
                case 3:
                    texture = texture4;
                    break;
                case 4:
                default:
                    texture = texture5;
                    break;
            }
        }catch (Exception ex){

        }
    }
    /**
     * Main method, Renders the trash at the right location of the screen, depending on
     * the pos[] vector.
     */
    public void render (float delta) {
        // if something happens to path below, delete trash
        IPlaceable underlying = world.findPlaceableByPos(getTilePos());
        if(underlying==null || underlying.getType()!=PlaceableType.PATH) world.removeTrash(this);
        // draw
        final float x = (pos[0] - pos[1]) * 100 / 2f + 50;
        final float y = (pos[0] + pos[1]) * 50 / 2f + 25;
        game.batch.draw(texture, x - 10, y, 20f, 20f);
    }
    /**
     * Memory Cleanup function that is executed in the Screen's respective function
     */
    public static void dispose() {
        texture1.dispose();
        texture2.dispose();
        texture3.dispose();
        texture4.dispose();
        texture5.dispose();
    }
    /**
     * Getters / Setters
     */
    public float[] getPos(){
        return pos;
    }
    public void setPos(float x, float y){
        pos[0] = x;
        pos[1] = y;
    }
    /**
     * Returns a rounded version of the pos, to decide which square the trash is on
     */
    public int[] getTilePos(){
        int x = Math.round(pos[0]);
        int y = Math.round(pos[1]);

        return new int[]{ x,y };
    }
    public void setBeingCollected(boolean isBeingCollected){
        this.isBeingCollected=isBeingCollected;
    }
    public boolean getBeingCollected(){
        return isBeingCollected;
    }
}
