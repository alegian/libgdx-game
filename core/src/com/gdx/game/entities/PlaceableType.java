package com.gdx.game.entities;

/**
 * Building Types Enumeration
 */
public enum PlaceableType {
    ROLLERCOASTER("rollercoaster", "game", 700, 7),
    BUMPER_CARS("bumper_cars", "game", 250, 3),
    CASTLE("castle", "game", 100, 2),
    CAROUSEL("carousel", "game", 250, 3),
    PENDULUM("pendulum", "game", 400, 4),
    SHIP("ship", "game", 350, 4),
    DROP_TOWER("drop_tower", "game", 400, 4),
    AIM("aim", "game", 100, 1),
    TRASHCAN("trashcan", "utility", 50, 0),
    TREE("tree", "utility", 50, 0),
    PATH("path", "utility", 20, 0),
    FOUNTAIN("fountain", "utility", 100, 0),
    CAFE("cafe", "shop", 600, 6),
    STAND1("stand1", "shop", 100, 2),
    STAND2("stand2", "shop", 100, 2),
    STAND3("stand3", "shop", 100, 2),
    STAND4("stand4", "shop", 100, 2),
    REPAIRMAN("repairman", "staff", 100, 0),
    REPAIRMAN_PREMIUM("repairman_gold", "staff", 300, 0),
    CLEANER("repairman", "staff", 80, 0),
    CLEANER_PREMIUM("repairman_gold", "staff", 300, 0);

    /**
     * Placeable Texture name
     */
    private final String textureName;
    /**
     * Placeable category (game, shop, staff, utility)
     */
    private String category;
    /**
     * Placeable default ticket cost
     */
    private final int originalTicketCost;
    /**
     * Placeable ticket cost (can change)
     */
    private int ticketCost;
    /**
     * Placeable build cost
     */
    private int cost;
    /**
     * Constructor, creates the Building's Texture name
     * @param textureName the Texture's name
     */
    PlaceableType(String textureName, String category, int cost, int ticketCost){
        this.textureName = textureName;
        this.category = category;
        this.cost=cost;
        this.ticketCost=ticketCost;
        this.originalTicketCost=ticketCost;
    }
    /**
     * getters / setters
     */
    public String getTextureName(){
        return textureName + ".png";
    }
    public String getCategory(){
        return category;
    }
    public int getTicketCost(){
        return ticketCost;
    }
    public int getCost(){
        return cost;
    }
    public int getOriginalTicketCost(){
        return originalTicketCost;
    }
    public void incrementTicketCost(){
        ticketCost++;
        if(ticketCost>3*originalTicketCost) ticketCost=3*originalTicketCost;
    }
    public void decrementTicketCost(){
        ticketCost--;
        if(ticketCost<0) ticketCost=0;
    }
    //getTicketCost();

}
