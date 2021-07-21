package com.gdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;
import com.gdx.game.utils.Distance;
import com.gdx.game.utils.Graph;
import com.gdx.game.utils.GraphNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Cleaner implements IPlaceable{
    /**
     * Static Texture
     */
    static Texture texture;
    static PlaceableType type;
    static Random random = new Random();
    HuntersGame game;
    World world;
    /**
     * what the guest is doing
     */
    StaffMode mode = StaffMode.NONE;
    /**
     * timer variable
     */
    int frameCounter= 0;
    /**
     * 2D position vector in interpolated Tile Coordinates
     */
    float[] pos;
    /**
     * 2D transient destination in interpolated Tile Coordinates
     */
    float[] microDestination;
    /**
     * if the repairman is the gold version or not
     */
    boolean premium;
    /**
     * 2D long term destination in Tile Coordinates
     */
    int[] macroDestination = null;
    /**
     * 2D home position in Tile Coordinates
     */
    int[] homePos;
    /**
     * Which trash the cleaner plans to go to
     */
    Trash destinationTrash = null;
    /**
     * Constructor, Links the World and Game instances, sets the home position
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param world the World instance to access placed buildings etc
     * @param x the cleaner's home X position in interpolated Tile Coordinates
     * @param y the cleaner's home Y position in interpolated Tile Coordinates
     * @param premium if the cleaner is the gold version or not
     */
    public Cleaner (HuntersGame game, World world, int x, int y, boolean premium){
        try {
            this.game = game;
            this.world = world;
            this.premium = premium;
            texture = new Texture("cleaner_human.png");
            pos = new float[]{x, y};
            homePos = new int[]{x, y};
            microDestination = pos;
            type = PlaceableType.CLEANER;
            if (premium) {
                type = PlaceableType.CLEANER_PREMIUM;
            }
        }catch (Exception ex){

        }
    }
    /**
     * Main method, Renders the repairman at the right location of the screen, depending on
     * the pos[] vector.
     */
    public void render (float delta) {
        final float x = (pos[0] - pos[1]) * 100 / 2f + 50;
        final float y = (pos[0] + pos[1]) * 50 / 2f + 25;
        game.batch.draw(texture, x - 15, y, 30f, 40f);
        update(delta);
    }
    @Override
    public PlaceableType getType() {
        if(premium) return PlaceableType.CLEANER_PREMIUM;
        return PlaceableType.CLEANER;
    }
    /**
     * Update method that runs the repairman's AI calculations
     */
    private void update(float delta){
        float speed = 1f;
        if(premium) speed=2f;
        if(mode == StaffMode.NONE){
            mode = StaffMode.WALKING;
        }else if(mode == StaffMode.WALKING){
            // choose new macro-destination, or follow the old one
            if(macroDestination==null){
                ArrayList<Trash> trash = world.getAvailableTrash(homePos, getRange());
                if(trash.size()>0){
                    Trash randomTrash = trash.get(random.nextInt(trash.size()));
                    destinationTrash=randomTrash;
                    destinationTrash.setBeingCollected(true);
                    macroDestination=randomTrash.getTilePos();
                }else{
                    macroDestination = homePos;
                }
            }else if(Math.abs(pos[0]-macroDestination[0])<0.1 && Math.abs(pos[1]-macroDestination[1])<0.1){
                if(macroDestination[0]==homePos[0] && macroDestination[1]==homePos[1]){
                    mode=StaffMode.NONE;
                    macroDestination = null;
                }else {
                    mode = StaffMode.WORKING;
                    frameCounter = 2 * 60;
                    macroDestination = null;
                }
            }else{
                // choose micro destination;
                microDestination = getNextMicroDestination();
                if(microDestination!=null) {
                    // calculate dx and dy
                    double x = microDestination[0] - pos[0];
                    double y = microDestination[1] - pos[1];
                    double angle = Math.atan2(y, x);
                    float dx = speed * delta * (float) Math.cos(angle);
                    float dy = speed * delta * (float) Math.sin(angle);
                    // update the position
                    float[] newPos = new float[]{0, 0};
                    newPos[0] = pos[0] + dx;
                    newPos[1] = pos[1] + dy;
                    pos = newPos;
                }
            }
        }else if(mode == StaffMode.WORKING){
            if(frameCounter==0){
                mode=StaffMode.NONE;
                if(destinationTrash != null){
                    world.removeTrash(destinationTrash);
                    destinationTrash.setBeingCollected(false);
                }
                destinationTrash=null;
            }else{
                frameCounter--;
            }
        }
    }
    /**
     * Gets Next micro-destination based on graph of paths
     */
    public float[] getNextMicroDestination(){
        float[] output = null;
        // reset old nodes
        Graph.resetNodes();
        // get all paths
        ArrayList<int[]> paths = world.getPathCoordinates();
        // can walk on top of home
        paths.add(homePos);
        // add a node for each path
        for (int[] path : paths) {
            Graph.addNode(new GraphNode(path));
        }
        // add adjacency
        for(GraphNode node : Graph.getNodes()){
            for(GraphNode n : Graph.getNodes()){
                int distance = Math.abs(node.pos[0]-n.pos[0])+Math.abs(node.pos[1]-n.pos[1]);
                if(node.isAdjacent(n)) node.addAdjacent(n, distance);
            }
        }
        // find current node
        GraphNode currNode = Graph.getNodeByPos(getTilePos());
        if(currNode!=null) {
            // run dijkstra from current node
            Graph.calculateShortestPathsFromRoot(currNode);
            // get the node representing the macro-destination and its path
            GraphNode destNode = Graph.getNodeByPos(macroDestination);
            if (destNode != null) {
                LinkedList<GraphNode> shortestPath = (LinkedList<GraphNode>) destNode.shortestPath;
                // if we are not there yet, pick a new micro-dest
                if(shortestPath.size()>=2) {
                    int[] intpos = shortestPath.get(1).pos;
                    output = new float[]{intpos[0], intpos[1]};
                }else{
                    output = new float[]{macroDestination[0], macroDestination[1]};
                }
            }
        }
        System.out.println(output);
        return output;
    }
    /**
     * Memory Cleanup function that is executed in the Screen's respective function
     */
    public static void dispose() {
        texture.dispose();
    }
    /**
     * Reset for when shop is opened
     */
    public void reset() {
        destinationTrash = null;
        pos=new float[]{homePos[0], homePos[1]};
        macroDestination = null;
        mode = StaffMode.NONE;
    }
    /**
     * Getters / Setters
     */
    public int getRange(){
        if(premium) return 5;
        return 3;
    }
    public float[] getPos(){
        return pos;
    }
    public void setPos(float x, float y){
        pos[0] = x;
        pos[1] = y;
    }
    public int[] getHomePos(){
        return homePos;
    }
    public void setHomePos(int x, int y){
        homePos[0] = x;
        homePos[1] = y;
    }
    public StaffMode getMode(){
        return mode;
    }
    public void setMode(StaffMode m){
        mode = m;
    }
    public int getCost(){
        return type.getCost();
    }
    @Override
    public ArrayList<Distance> getOverlappingTiles() {
        return new ArrayList<>();
    }
    @Override
    public int[] getPlacementPos() {
        return getHomePos();
    }
    /**
     * Returns a rounded version of the pos, to decide which square the repairman is on
     */
    public int[] getTilePos(){
        int x = Math.round(pos[0]);
        int y = Math.round(pos[1]);

        return new int[]{ x,y };
    }

    public boolean getPremium(){
        return premium;
    }
}
