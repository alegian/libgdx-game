package com.gdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.game.HuntersGame;
import com.gdx.game.utils.Graph;
import com.gdx.game.utils.GraphNode;

import java.util.*;

/**
 * Represents a Guest in the World
 */
public class Guest {
    /**
     * Static Texture
     */
    static Texture texture ;
    static Random random = new Random();
    HuntersGame game;
    World world;
    /**
     * what the guest is doing
     */
    GuestMode mode = GuestMode.NONE;
    /**
     * guest's mood. When it reaches 0 guest wants to leave the park
     */
    float mood = 10f;
    /**
     * timer variable
     */
    int frameCounter= 0;
    /**
     * number of attractions the guest has used
     */
    int attractionsVisited= 0;
    /**
     * if the guest wants to throw trash
     */
    boolean hasTrash = false;
    /**
     * 2D position vector in interpolated Tile Coordinates
     */
    float[] pos = new float[]{0,0};
    /**
     * 2D transient destination in interpolated Tile Coordinates
     */
    float[] microDestination;
    /**
     * 2D long term destination in Tile Coordinates
     */
    int[] macroDestination = new int[]{0,0};
    /**
     * Which building the guest plans to go to
     */
    Building destinationBuilding = null;
    /**
     * Constructor, Links the World and Game instances, sets the Overlapping Tiles and initializes
     * the position
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param world the World instance to access placed buildings etc
     */
    public Guest (HuntersGame game, World world){
        try {
            this.game = game;
            this.world = world;
            float offsetX = random.nextFloat() / 4;
            float offsetY = random.nextFloat() / 4;
            texture = new Texture("guest.png");
            int choice = random.nextInt(2);
            switch (choice) {
                case 0:
                    pos = new float[]{11 + offsetX, -11 + offsetY};
                    break;
                case 1:
                    pos = new float[]{12 + offsetX, -11 + offsetY};
                    break;
            }
            microDestination = pos;
            macroDestination = null;
        }catch(Exception ex){

        }
    }
    /**
     * Main method, Renders the Guest at the right location of the screen, depending on
     * the pos[] vector.
     */
    public void render (float delta) {
        if(!(mode == GuestMode.USING && destinationBuilding.usersVanish())) {
            final float x = (pos[0] - pos[1]) * 100 / 2f + 50;
            final float y = (pos[0] + pos[1]) * 50 / 2f + 25;
            game.batch.draw(texture, x - 15, y, 30f, 40f);
        }
        update(delta);
    }
    /**
     * Update method that runs the guest's AI calculations
     */
    public void update(float delta){
        float speed = 1f;
        if(mode == GuestMode.NONE){
            mode = GuestMode.WALKING;
            // if guest visits too many attractions or their mood goes to 0, leave the park
            if(attractionsVisited>1 || mood==0f) {
                macroDestination = new int[]{11, -11};
                destinationBuilding = null;
            }
        }
        else if(mode == GuestMode.WALKING){
            // if the guest has trash, try to find trashcan
            Building trashcan = world.getNearbyTrashcan(getTilePos()[0], getTilePos()[1]);
            if(hasTrash && trashcan !=null){
                macroDestination = world.getNearbyPath(trashcan).getPos();
                destinationBuilding=trashcan;
            }
            // choose new macro-destination, or follow the old one
            if(macroDestination==null){
                // choose a random attraction from all available ones
                ArrayList<Building> attractions = world.getAvailableAttractions();
                if(attractions.size()>0){
                    Building randomAttraction = attractions.get(random.nextInt(attractions.size()));
                    destinationBuilding=randomAttraction;
                    macroDestination=world.getNearbyPath(randomAttraction).getPos();
                }
            }
            // if guest is within 0.1 distance of the destination (practically arrived)
            else if(Math.abs(pos[0]-macroDestination[0])<0.1 && Math.abs(pos[1]-macroDestination[1])<0.1){
                // if destination is the park's exit, then leave
                if(macroDestination[0]==11 && macroDestination[1]==-11){
                    world.removeGuest(this);
                }
                // if destination is a trashcan, try to throw trash
                else if(destinationBuilding!=null && destinationBuilding.getType() == PlaceableType.TRASHCAN){
                    mode = GuestMode.NONE;
                    hasTrash=false;
                    destinationBuilding=null;
                    macroDestination = null;
                }
                // default behavior, try to enter an attraction or wait outside
                else {
                    mode = GuestMode.WAITING;
                    frameCounter = 5 * 60;
                    if (destinationBuilding != null) {
                        // pay
                        Economy.addMoney(destinationBuilding.getTicketCost());
                        // add to attraction's waiting queue
                        destinationBuilding.queueGuest(this);
                        // add mood
                        increaseMood(1f);
                        // if attraction is overpriced, lower mood
                        if(destinationBuilding.getType().getTicketCost()>1.1f*destinationBuilding.getType().getOriginalTicketCost()){
                            decreaseMood(0.5f);
                        }
                    }
                    macroDestination = null;
                    attractionsVisited++;
                }
            }else{
                // if guest has trash and no trashcan around, possibly throw trash
                if(destinationBuilding!=null && destinationBuilding.getType() != PlaceableType.TRASHCAN && hasTrash){
                    int diceRoll = random.nextInt(4*60);
                    // if he decides to throw
                    if(diceRoll == 150){
                        hasTrash=false;
                        world.addTrash(getTilePos()[0], getTilePos()[1]);
                    }
                }
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
        }
        else if(mode == GuestMode.USING){
            if(frameCounter==0){
                if(destinationBuilding!=null) {
                    if (destinationBuilding.getType() == PlaceableType.CAFE || destinationBuilding.getType() == PlaceableType.STAND1 || destinationBuilding.getType() == PlaceableType.STAND2 || destinationBuilding.getType() == PlaceableType.STAND3 || destinationBuilding.getType() == PlaceableType.STAND4) {
                        hasTrash = true;
                    }
                    destinationBuilding.dequeueGuest();
                }
                mode=GuestMode.NONE;
            }else{
                frameCounter--;
            }
        }
        else if(mode == GuestMode.WANDERING){
            if(frameCounter==0){
                mode=GuestMode.WALKING;
            }else{
                frameCounter--;
            }
        }
        else if(mode == GuestMode.WAITING){
            if(mood==0f){
                mode = GuestMode.WALKING;
                destinationBuilding.removeGuest(this);
                macroDestination = new int[]{11, -11};
                destinationBuilding = null;
            }
        }
        passiveMoodUpdate();
    }
    /**
     * Changes the guest's mood passively based on trash and attractions like fountains
     */
    private void passiveMoodUpdate(){
        // decrease mood per trash
        float moodToSubtract=world.trash.size()*0.0005f;
        // decrease mood for waiting in queues
        if(mode == GuestMode.WAITING){
            moodToSubtract+=0.004;
        }
        decreaseMood(moodToSubtract);
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
        return output;
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
    public static void dispose() {
        texture.dispose();
    }
    /**
     * increase the mood with max of 10
     */
    public void increaseMood(float delta) {
        mood+=delta*(1f + world.getMoodBuff());
        if(mood>10f) mood = 10f;
    }
    /**
     * decrease the mood with min of 0
     */
    public void decreaseMood(float delta) {
        mood-=delta*(1f - world.getMoodBuff());
        if(mood<0f) mood = 0f;
    }
    /**
     * Getters / Setters
     */
    public float[] getPos(){
        return pos;
    }
    public float getMood(){
        return mood;
    }
    public void setPos(float x, float y){
        pos[0] = x;
        pos[1] = y;
    }
    public GuestMode getMode(){
        return mode;
    }
    public void setMode(GuestMode m){
        mode = m;
    }
    /**
     * Returns a rounded version of the pos, to decide which square the guest is on
     */
    public int[] getTilePos(){
        int x = Math.round(pos[0]);
        int y = Math.round(pos[1]);

        return new int[]{ x,y };
    }

    public void setDestinationBuilding(Building b){
        destinationBuilding = b;
    }

    public void setMacroDestination(int x, int y){
        macroDestination[0] = x;
        macroDestination[1] = y;
    }
}

