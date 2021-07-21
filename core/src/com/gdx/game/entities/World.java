package com.gdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.HuntersGame;
import com.gdx.game.utils.Distance;
import com.gdx.game.utils.PositionUtils;
import com.gdx.game.utils.RenderUtils;
//import sun.awt.image.BufferedImageDevice;

import java.util.ArrayList;


/**
 * Represents the in-game World (anything rendered below the UI)
 */
public class World {
    HuntersGame game;
    /**
     * The list of all placed Buildings
     */
    public static ArrayList<IPlaceable> placeables = new ArrayList<>();
    /**
     * The list of selected tiles (used to draw selector)
     */
    public static ArrayList<int[]> selectedTiles = new ArrayList<>();
    /**
     * The list of all guests
     */
    ArrayList<Guest> guests = new ArrayList<>();
    /**
     * The list of guests that need deletion
     */
    ArrayList<Guest> guestsToDelete = new ArrayList<>();
    /**
     * The list of all trash
     */
    ArrayList<Trash> trash = new ArrayList<>();
    /**
     * The list of trash that need deletion
     */
    ArrayList<Trash> trashToDelete = new ArrayList<>();
    /**
     * The number of frames since the last time a guest spawned
     */
    int framesSinceGuest = 0;
    ShapeRenderer shapeRenderer;
    /**
     * Texture for the Tile Selector (blue outline of tile)
     */
    Texture selectorImg;
    /**
     * Controls if the selector square is visible
     */
    public static boolean selectorVisible = false;
    /**
     * Controls if the square grid is visible
     */
    public static boolean tilesVisible = false;
    /**
     * Constructor, Links the Game and ShapeRenderer instances and initializes the selector
     * texture and the buildings array
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param shapeRenderer the ShapeRenderer instance provided by the GameScreen, used
     *                      for rendering geometric objects (in this case line segments)
     */
    public World (HuntersGame game, ShapeRenderer shapeRenderer){
        try {
            this.game = game;
            this.shapeRenderer = shapeRenderer;
            selectorImg = new Texture("selector.png");
            // create 2 paths as entry for guests
            Building entry1 = new Building(game, PlaceableType.PATH, 11, -11);
            Building entry2 = new Building(game, PlaceableType.PATH, 12, -11);
            entry1.initTexture();
            entry2.initTexture();
            placeables.add(entry1);
            placeables.add(entry2);
        }catch(Exception ex){

        }
    }
    /**
     * First Render-Helper function: draws the square grid
     */
    private void drawTiles(){
        if(tilesVisible){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 0, 0, 1);

            for(int i =0; i<30; i++){
                for(int j =-15; j<15; j++) {
                    drawTile(i, j);
                }
            }

            shapeRenderer.end();
        }
    }
    /**
     * Helper function for drawTiles(): draws one Tile at position given
     * @param x the tile's horizontal position
     * @param y the tile's vertical position
     */
    private void drawTile(int x, int y){
        final int height=50;
        final int width=100;
        final float translationX = (x-y)*width/2f;
        final float translationY = (x+y)*height/2f;
        if(translationX >= 0 && width+translationX<=1260 && translationY >= 0 && height+translationY<=700) {
            shapeRenderer.line(translationX, height / 2f + translationY, width / 2 + translationX, height + translationY);
            shapeRenderer.line(translationX, height / 2f + translationY, width / 2 + translationX, translationY);
            shapeRenderer.line(width + translationX, height / 2f + translationY, width / 2 + translationX, height + translationY);
            shapeRenderer.line(width + translationX, height / 2f + translationY, width / 2 + translationX, translationY);
        }
    }
    /**
     * Second Render-Helper function: draws the Tile Selector (blue outline of selected square)
     */
    private void drawSelector(){
        if(selectorVisible) {
            int[] pos = RenderUtils.getPositionFromMouse();
            selectedTiles.add(pos);
            for(int[] t : selectedTiles) {
                final float x = (t[0] - t[1]) * 100 / 2f;
                final float y = (t[0] + t[1]) * 50 / 2f;
                if (x >= 0 && 100 + x <= 1260 && y >= 0 && 50 + y <= 700) {
                    game.batch.begin();
                    game.batch.draw(selectorImg, x, y, 100f, 50f);
                    game.batch.end();
                }
            }
        }
        selectedTiles.clear();
    }
    /**
     * Main method, Renders the Tile Grid and Selector(if needed) and the buildings (by calling
     * the respective render function)
     */
    public void render (float delta) {
        update();
        drawTiles();
        drawSelector();
        game.batch.begin();

        // draw in correct order for depth
        for(int i=25; i>-26; i--){
            for(int j=25; j>-26; j--){
                // draw buildings
                for( IPlaceable b : placeables){
                    if(b instanceof Building) {
                        if (b.getPlacementPos()[0] == i && b.getPlacementPos()[1] == j) b.render(delta);
                    }
                }
                // draw trash
                for( Trash t : trash){
                    if (t.getTilePos()[0] == i && t.getTilePos()[1] == j) t.render(delta);
                }
                // draw guests
                for( Guest g : guests){
                    if(Math.floor(g.getPos()[0])==i && Math.floor(g.getPos()[1])==j) g.render(delta);
                }
                // draw staff
                for( IPlaceable s : placeables){
                    if(s instanceof Repairman) {
                        if (((Repairman) s).getTilePos()[0] == i && ((Repairman) s).getTilePos()[1] == j) s.render(delta);
                    }
                    if(s instanceof Cleaner) {
                        if (((Cleaner) s).getTilePos()[0] == i && ((Cleaner) s).getTilePos()[1] == j) s.render(delta);
                    }
                }
            }
        }
        game.batch.end();
    }
    /**
     * logic ran every frame
     */
    public void update(){
        // delete collected guests
        for(Guest g : guestsToDelete){
            guests.remove(g);
        }
        guestsToDelete.clear();
        // delete collected trash
        for(Trash t : trashToDelete){
            trash.remove(t);
        }
        trashToDelete.clear();
        // if shop is open reset everything
        if(UserInterface.editVisible){
            clearForEdit();
        }
        // otherwise spawn guests slowly
        else {
            int numAttractions = getAvailableAttractions().size();
            if (numAttractions != 0) {
                int limit = 60 * 60 * 2 / (numAttractions * 3);
                if (framesSinceGuest > limit) {
                    framesSinceGuest = 0;
                    Guest newGuest = new Guest(game, this);
                    // if added guest successfully, pay money
                    if(addGuest(newGuest)){
                        Economy.addMoney(2);
                    }
                } else {
                    framesSinceGuest++;
                }
            } else {
                framesSinceGuest++;
            }
        }
    }
    /**
     * On-Click code injected to the Screen's Click Handler
     * @param x the click's horizontal position
     * @param y the click's vertical position
     */
    public void onClick(int x, int y){

    }
    /**
     * Memory Cleanup function that is executed in the Screen's respective function
     */
    public void dispose() {
        selectorImg.dispose();
        for( IPlaceable b : placeables){
            if(b instanceof Building){
                ((Building) b).dispose();
            }
        }
        Trash.dispose();
        Guest.dispose();
        Repairman.dispose();
    }
    /**
     * Adds a new Placeable to the "placed" list
     */
    public boolean addPlaceable(IPlaceable newPlaceable){
        if(newPlaceable!=null) {
            // if there is overlap, fail
            for (IPlaceable p : placeables) {
                if (PositionUtils.overlaps(p, newPlaceable)) {
                    return false;
                }
            }
            // else check price
            if(!Economy.subtractMoney(newPlaceable.getCost())){
                return false;
            }
            try {
                // if it is a building, init the texture
                if (newPlaceable instanceof Building) ((Building) newPlaceable).initTexture();
            }catch (Exception ex){

            }
            // place down
            placeables.add(newPlaceable);
            return true;
        }
        return false;
    }

    /**
     * Same as addPlaceable but subtracts no money
     */
    public boolean movePlaceable(IPlaceable newPlaceable){
        if(newPlaceable!=null) {
            // if there is overlap, fail
            for (IPlaceable p : placeables) {
                if (PositionUtils.overlaps(p, newPlaceable)) {
                    return false;
                }
            }
            // if it is a building, init the texture
            if(newPlaceable instanceof Building) ((Building)newPlaceable).initTexture();
            // place down
            placeables.add(newPlaceable);
            return true;
        }
        return false;
    }
    /**
     * Adds a new Guest to the guests list
     */
    public boolean addGuest(Guest newGuest){
        if(newGuest!=null) {
            return guests.add(newGuest);
        }
        return false;
    }
    /**
     * Removes a Guest after too many visits
     */
    public boolean removeGuest(Guest g){
        return guestsToDelete.add(g);
    }
    /**
     * Adds new Trash to the trash list
     */
    public boolean addTrash(int x, int y){
        Trash newTrash = new Trash(game, this, x, y);
        return trash.add(newTrash);
    }
    /**
     * Removes Trash from list
     */
    public boolean removeTrash(Trash t){
        return trashToDelete.add(t);
    }
    /**
     * Removes a Placeable from the world
     */
    public boolean removePlaceable(IPlaceable b){
        if(b!=null) {
            placeables.remove(b);
            // if it is a building, dispose the texture
            if(b instanceof Building) ((Building)b).dispose();
            return true;
        }
        return false;
    }
    /**
     * Returns a "placed" instance based on 2D coords
     */
    public IPlaceable findPlaceableByPos(int[] pos){
        IPlaceable output = null;
        for(IPlaceable p : placeables){
            if(PositionUtils.samePos(p.getPlacementPos(),pos)){
                output = p;
            }
            for(Distance d : p.getOverlappingTiles()){
                if(PositionUtils.samePos(new int[]{p.getPlacementPos()[0]+d.dx, p.getPlacementPos()[1]+d.dy}, pos)) output = p;
            }
        }
        return output;
    }
    /**
     * Returns all attraction buildings that have adjacent path
     */
    public ArrayList<Building> getAvailableAttractions(){
        ArrayList<Building> output = new ArrayList<>();
        for(IPlaceable p : placeables){
            if(p instanceof Building) {
                Building b = (Building) p;
                PlaceableType type = b.getType();
                if (type != PlaceableType.PATH && type != PlaceableType.TRASHCAN && type != PlaceableType.FOUNTAIN && type != PlaceableType.TREE) {
                    if (getNearbyPath(b) != null) {
                        output.add(b);
                    }
                }
            }
        }
        return output;
    }
    /**
     * Returns all available trash for a cleaner
     */
    public ArrayList<Trash> getAvailableTrash(int[] homePos, int range){
        ArrayList<Trash> output = new ArrayList<>();
        for(Trash t : trash){
            if(!t.getBeingCollected() && Math.abs(t.getTilePos()[0]-homePos[0])<range+1 && Math.abs(t.getTilePos()[1]-homePos[1])<range+1){
                output.add(t);
            }
        }
        return output;
    }
    /**
     * Returns all broken buildings that have adjacent path
     */
    public ArrayList<Building> getAvailableBrokenAttractions(){
        ArrayList<Building> output = new ArrayList<>();
        for(IPlaceable p : placeables){
            if(p instanceof Building) {
                Building b = (Building) p;
                PlaceableType type = b.getType();
                // not a 'utility building' and 'broken' and 'not under repair'
                if (type != PlaceableType.PATH && type != PlaceableType.TRASHCAN && type != PlaceableType.FOUNTAIN && type != PlaceableType.TREE && b.getBroken() && !b.getBeingRepaired()) {
                    if (getNearbyPath(b) != null) {
                        output.add(b);
                    }
                }
            }
        }
        return output;
    }
    /**
     * Returns a path that is adjacent to given building
     */
    public Building getNearbyPath(Building b){
        Building output = null;
        Building curr = null;
        int[] pos = b.getPos();
        // distance -1, 0
        IPlaceable current = findPlaceableByPos(new int[]{pos[0]-1, pos[1]});
        if(current instanceof Building){
            curr=(Building) current;
            if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
        }
        // distance 1, 0
        current = findPlaceableByPos(new int[]{pos[0]+1, pos[1]});
        if(current instanceof Building){
            curr=(Building) current;
            if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
        }
        // distance 0,-1
        current = findPlaceableByPos(new int[]{pos[0], pos[1]-1});
        if(current instanceof Building){
            curr=(Building) current;
            if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
        }
        // distance 0, 1
        current = findPlaceableByPos(new int[]{pos[0], pos[1]+1});
        if(current instanceof Building){
            curr=(Building) current;
            if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
        }

        for(Distance d : b.getOverlappingTiles()){
            // distance -1, 0
            current = findPlaceableByPos(new int[]{pos[0]+d.dx-1, pos[1]+d.dy});
            if(current instanceof Building){
                curr=(Building) current;
                if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
            }
            // distance 1, 0
            current = findPlaceableByPos(new int[]{pos[0]+d.dx+1, pos[1]+d.dy});
            if(current instanceof Building){
                curr=(Building) current;
                if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
            }
            // distance 0,-1
            current = findPlaceableByPos(new int[]{pos[0]+d.dx, pos[1]+d.dy-1});
            if(current instanceof Building){
                curr=(Building) current;
                if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
            }
            // distance 0, 1
            current = findPlaceableByPos(new int[]{pos[0]+d.dx, pos[1]+d.dy+1});
            if(current instanceof Building){
                curr=(Building) current;
                if(curr!=null && curr.getType()== PlaceableType.PATH) return curr;
            }
        }
        return output;
    }
    /**
     * Gets a nearby Trashcan
     */
    Building getNearbyTrashcan(int x, int y){
        for(IPlaceable b : placeables){
            PlaceableType type = b.getType();
            int[] pos = b.getPlacementPos();
            // if trashcan and close to given (x,y) return it
            if(type==PlaceableType.TRASHCAN && Math.abs(x-pos[0])<3 && Math.abs(y-pos[1])<3) return (Building)b;
        }
        return null;
    }
    /**
     * Returns all path locations
     */
    public ArrayList<int[]> getPathCoordinates(){
        ArrayList<int[]> output = new ArrayList<>();
        for(IPlaceable b : placeables){
            if(b instanceof Building) {
                if (((Building)b).getType() == PlaceableType.PATH) {
                    output.add(((Building)b).getPos());
                }
            }
        }
        return output;
    }
    /**
     * Resets guests and staff
     */
    public void clearForEdit(){
        guests = new ArrayList<>();
        framesSinceGuest = 0;
        for(IPlaceable p : placeables){
            if(p instanceof Building) {
                ((Building)p).resetGuestQueue();
            }
            if(p instanceof Repairman) {
                ((Repairman)p).reset();
            }
            if(p instanceof Cleaner) {
                ((Cleaner)p).reset();
            }
        }
        for(Trash t: trash){
            t.setBeingCollected(false);
        }
    }
    /**
     * Resets guests and staff
     */
    public float getMoodBuff(){
        float buff = 0f;
        for(IPlaceable p : placeables){
            if(p.getType() == PlaceableType.TREE){
                buff+=0.01f;
            }
            else if(p.getType() == PlaceableType.FOUNTAIN){
                buff+=0.04f;
            }
        }
        return buff;
    }

    public ArrayList<Guest> getGuests(){
        return guests;
    }
    public ArrayList<Trash> getTrash(){
        return trash;
    }
}
