package com.gdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gdx.game.HuntersGame;
import com.gdx.game.utils.Distance;
import com.gdx.game.utils.PositionUtils;
import com.gdx.game.utils.RenderUtils;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Responsible for all things UI
 */
public class UserInterface extends Component {
    /**
     * All UI Textures
     */
    Texture buttonImg, topbarImg, shopImg, staffShopImg, rollercoasterImg, bumperCarsImg,
            castleImg, carouselImg, pendulumImg, shipImg, dropTowerImg, aimImg, editButtonImg,
            cafeImg, stand1Img, stand2Img, stand3Img, stand4Img, trashcanImg, treeImg, pathImg,
            fountainImg, cleanerImg, cleanerGoldImg, repairmanImg, repairmanGoldImg, editPriceButtonImg;
    HuntersGame game;
    World world;
    BitmapFont font = new BitmapFont();
    /**
     * Time in frames
     */
    int time = 0;
    /**
     * Average guest mood
     */
    float avgMood = 0f;
    /**
     * Varying Texture array, used for dynamic Shop Tabs
     */
    Texture[] textures;
    /**
     * Maps from Selected Item Index to Placeable Types
     */
    HashMap<Integer, PlaceableType> gameIndexToType;
    HashMap<Integer, PlaceableType> shopIndexToType;
    HashMap<Integer, PlaceableType> utilityIndexToType;
    HashMap<Integer, PlaceableType> staffIndexToType;
    /**
     * Constructor, Links the World and Game instances and initializes the textures array
     * @param game the Game instance provided by the GameScreen, contains the sprite batch
     *             which is essential for rendering
     * @param world the World instance provided by the GameScreen, contains all game objects
     *              and relevant information
     */
    public UserInterface (HuntersGame game, World world){
        this.game = game;
        this.world = world;
        textures = new Texture[8];
        // init hash maps
        gameIndexToType = new HashMap<>();
        gameIndexToType.put(0, PlaceableType.ROLLERCOASTER);
        gameIndexToType.put(1, PlaceableType.BUMPER_CARS);
        gameIndexToType.put(2, PlaceableType.CASTLE);
        gameIndexToType.put(3, PlaceableType.CAROUSEL);
        gameIndexToType.put(4, PlaceableType.PENDULUM);
        gameIndexToType.put(5, PlaceableType.SHIP);
        gameIndexToType.put(6, PlaceableType.DROP_TOWER);
        gameIndexToType.put(7, PlaceableType.AIM);
        shopIndexToType = new HashMap<>();
        shopIndexToType.put(0, PlaceableType.CAFE);
        shopIndexToType.put(1, PlaceableType.STAND1);
        shopIndexToType.put(2, PlaceableType.STAND2);
        shopIndexToType.put(3, PlaceableType.STAND3);
        shopIndexToType.put(4, PlaceableType.STAND4);
        shopIndexToType.put(5, null);
        shopIndexToType.put(6, null);
        shopIndexToType.put(7, null);
        utilityIndexToType = new HashMap<>();
        utilityIndexToType.put(0, PlaceableType.TRASHCAN);
        utilityIndexToType.put(1, PlaceableType.TREE);
        utilityIndexToType.put(2, PlaceableType.PATH);
        utilityIndexToType.put(3, PlaceableType.FOUNTAIN);
        utilityIndexToType.put(4, null);
        utilityIndexToType.put(5, null);
        utilityIndexToType.put(6, null);
        utilityIndexToType.put(7, null);
        staffIndexToType = new HashMap<>();
        staffIndexToType.put(0, PlaceableType.CLEANER);
        staffIndexToType.put(1, PlaceableType.CLEANER_PREMIUM);
        staffIndexToType.put(2, PlaceableType.REPAIRMAN);
        staffIndexToType.put(3, PlaceableType.REPAIRMAN_PREMIUM);
        staffIndexToType.put(4, null);
        staffIndexToType.put(5, null);
        staffIndexToType.put(6, null);
        staffIndexToType.put(7, null);
    }
    /**
     * Controls if the edit menu is visible
     */
    public static boolean editVisible = false;
    /**
     * Controls if the building shop is visible
     */
    public static boolean shopVisible = false;
    /**
     * Controls if the staff shop is visible
     */
    public static boolean staffShopVisible = false;
    /**
     * Controls tab of the building shop. Values = {games, shops, utility}
     */
    private String shopTab = "games";
    /**
     * Type of the Selected Placeable. null means none selected
     */
    private PlaceableType selectedPlaceableType = null;
    /**
     * Building that is being edited
     */
    private IPlaceable placeableToEdit = null;
    /**
     * List of all Game Buildings Textures
     */
    private Texture[] gamesTextures;
    /**
     * List of all Shops Buildings Textures
     */
    private Texture[] shopsTextures;
    /**
     * List of all Utility Buildings Textures
     */
    private Texture[] utilityTextures;
    /**
     * List of all Staff Textures
     */
    private Texture[] staffTextures;
    /**
     * Types of editing
     */
    enum EditModes{DELETE, MOVE, CHANGE_PRICE, NONE}
    /**
     * Current Edit Mode
     */
    public static EditModes editMode = EditModes.NONE;
    /**
     * Creates Instances for all Textures
     */
    public void initTextures(){
        buttonImg = new Texture("button.png");
        topbarImg = new Texture("top_bar.png");
        shopImg = new Texture("shop.png");
        staffShopImg= new Texture("staff_shop.png");
        rollercoasterImg= new Texture("rollercoaster.png");
        bumperCarsImg= new Texture("bumper_cars.png");
        castleImg= new Texture("castle.png");
        carouselImg= new Texture("carousel.png");
        pendulumImg= new Texture("pendulum.png");
        shipImg= new Texture("ship.png");
        dropTowerImg= new Texture("drop_tower.png");
        aimImg= new Texture("aim.png");
        cafeImg= new Texture("cafe.png");
        stand1Img= new Texture("stand1.png");
        stand2Img= new Texture("stand2.png");
        stand3Img= new Texture("stand3.png");
        stand4Img= new Texture("stand4.png");
        trashcanImg= new Texture("trashcan.png");
        treeImg= new Texture("tree.png");
        pathImg= new Texture("path.png");
        fountainImg= new Texture("fountain.png");
        cleanerImg= new Texture("cleaner.png");
        cleanerGoldImg= new Texture("cleaner_gold.png");
        repairmanImg= new Texture("repairman.png");
        repairmanGoldImg= new Texture("repairman_gold.png");
        editButtonImg = new Texture("edit_button.png");
        editPriceButtonImg = new Texture("edit_price_button.png");

        gamesTextures=new Texture[] {
                rollercoasterImg,
                bumperCarsImg,
                castleImg,
                carouselImg,
                pendulumImg,
                shipImg,
                dropTowerImg,
                aimImg
        };
        //filled with nulls to size 8, makes looping easier
        shopsTextures=new Texture[] {
                cafeImg,
                stand1Img,
                stand2Img,
                stand3Img,
                stand4Img,
                null,
                null,
                null
        };
        //filled with nulls to size 8, makes looping easier
        utilityTextures=new Texture[] {
                trashcanImg,
                treeImg,
                pathImg,
                fountainImg,
                null,
                null,
                null,
                null
        };
        //filled with nulls to size 8, makes looping easier
        staffTextures=new Texture[] {
                cleanerImg,
                cleanerGoldImg,
                repairmanImg,
                repairmanGoldImg,
                null,
                null,
                null,
                null
        };
    }
    /**
     * Main method, Renders everything (split into 4 helper functions)
     */
    public void render (float delta) {
        drawButtons();
        drawTopBar();
        drawShop();
        drawStaffShop();
        drawEdit();
        drawSelectedItem(delta);
        time++;
    }
    /**
     * On-Click code injected to the Screen's Click Handler
     * @param x the click's horizontal position
     * @param y the click's vertical position
     */
    public void onClick(int x, int y){
        // if clicked on exit (top right)
        if(x>=Gdx.graphics.getWidth()*0.965f && x<=Gdx.graphics.getWidth() && y>= 0 && y<=Gdx.graphics.getHeight()*0.06f){
            // close
            int response = JOptionPane.showConfirmDialog(this, "Do you want to leave the game?","EXIT",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        // if clicked the edit button
        else if(x>=0 && x<=40 && y>= Gdx.graphics.getHeight()/2+20 && y<=Gdx.graphics.getHeight()/2+60){
            editMode = EditModes.NONE;
            selectedPlaceableType = null;
            placeableToEdit = null;
            if(shopVisible){
                shopVisible = false;
                editVisible = true;
            }else if (staffShopVisible){
                staffShopVisible = false;
                editVisible = true;
            }else if (editVisible){
                World.tilesVisible = false;
                editVisible = false;
                World.selectorVisible = false;
            } else{
                World.tilesVisible = true;
                editVisible = true;
                World.selectorVisible = true;
            }
        }
        // if clicked the building shop button
        else if(x>=0 && x<=40 && y>=Gdx.graphics.getHeight()/2-20 && y<=Gdx.graphics.getHeight()/2+20){
            selectedPlaceableType=null;
            if(shopVisible){
                shopVisible = false;
                World.tilesVisible = false;
                World.selectorVisible = false;
            }else if (staffShopVisible){
                staffShopVisible = false;
                shopVisible = true;
            }else if (editVisible){
                editVisible = false;
                shopVisible = true;
            } else{
                World.tilesVisible = true;
                shopVisible = true;
                World.selectorVisible = true;
            }
        }
        // if clicked the staff shop button
        else if(x>=0 && x<=40 && y>=Gdx.graphics.getHeight()/2-60 && y<=Gdx.graphics.getHeight()/2-20){
            if(shopVisible){
                shopVisible = false;
                staffShopVisible = true;
            }else if (staffShopVisible){
                staffShopVisible = false;
                World.tilesVisible = false;
                World.selectorVisible = false;
            }else if (editVisible){
                editVisible = false;
                staffShopVisible = true;
            } else{
                World.tilesVisible = true;
                staffShopVisible = true;
                World.selectorVisible = true;
            }
        }
        // none of the above, and shop is open
        else if(shopVisible){
            //if clicked the games tab
            if(x>=Gdx.graphics.getWidth() * 0.15f && x<=Gdx.graphics.getWidth() * 0.15f +110 && y>=Gdx.graphics.getHeight() * 0.7f && y<=Gdx.graphics.getHeight() * 0.7f +40){
                shopTab="games";
                selectedPlaceableType=null;
            }
            //if clicked the shops tab
            else if(x>=Gdx.graphics.getWidth() * 0.15f +110 && x<=Gdx.graphics.getWidth() * 0.15f +220 && y>=Gdx.graphics.getHeight() * 0.7f && y<=Gdx.graphics.getHeight() * 0.7f+40){
                shopTab="shops";
                selectedPlaceableType=null;
            }
            //if clicked the utility tab
            else if(x>=Gdx.graphics.getWidth() * 0.15f +220&& x<=Gdx.graphics.getWidth() * 0.15f +330 && y>=Gdx.graphics.getHeight() * 0.7f && y<=Gdx.graphics.getHeight() * 0.7f+40){
                shopTab="utility";
                selectedPlaceableType=null;
            }
            //if clicked on an item
            else if(x>=Gdx.graphics.getWidth() * 0.15f && x<=Gdx.graphics.getWidth() * 0.85f && y>=Gdx.graphics.getHeight() * 0.7f + 40 && y<=Gdx.graphics.getHeight()){
                int selectedItemIndex = (int)Math.floor((x-Gdx.graphics.getWidth() * 0.15f) / (Gdx.graphics.getWidth() * 0.7f/8f)); //should have values between 0 and 7
                HashMap<Integer, PlaceableType> indexToType = gameIndexToType;
                switch (shopTab){
                    case "shops":
                        indexToType = shopIndexToType;
                        break;
                    case "utility":
                        indexToType = utilityIndexToType;
                        break;
                }
                selectedPlaceableType = indexToType.get(selectedItemIndex);
                if(selectedPlaceableType!=null){
                    shopVisible=false;
                }
            }
        }
        // none of the above, and staff shop is open
        else if(staffShopVisible){
            //if clicked on an item
            if(x>=Gdx.graphics.getWidth() * 0.15f && x<=Gdx.graphics.getWidth() * 0.85f && y>=Gdx.graphics.getHeight() * 0.7f + 40 && y<=Gdx.graphics.getHeight()){
                int selectedItemIndex = (int)Math.floor((x-Gdx.graphics.getWidth() * 0.15f) / (Gdx.graphics.getWidth() * 0.7f/8f)); //should have values between 0 and 7
                HashMap<Integer, PlaceableType> indexToType = staffIndexToType;
                selectedPlaceableType = indexToType.get(selectedItemIndex);
                if(selectedPlaceableType!=null){
                    staffShopVisible=false;
                }
            }
        }
        //if clicked with a selected item
        else if(selectedPlaceableType!=null && !editVisible){
            if(x>=0 && x<=Gdx.graphics.getWidth() && y>=0&& y<=Gdx.graphics.getHeight()){
                int[] pos = RenderUtils.getPositionFromMouse();
                if(selectedPlaceableType == PlaceableType.REPAIRMAN || selectedPlaceableType == PlaceableType.REPAIRMAN_PREMIUM ){
                    boolean premium = selectedPlaceableType == PlaceableType.REPAIRMAN_PREMIUM;
                    if (world.addPlaceable(new Repairman(game, world, pos[0], pos[1], premium))) {
                        selectedPlaceableType=null;
                        staffShopVisible=true;
                    }
                }else if(selectedPlaceableType == PlaceableType.CLEANER || selectedPlaceableType == PlaceableType.CLEANER_PREMIUM ){
                    boolean premium = selectedPlaceableType == PlaceableType.CLEANER_PREMIUM;
                    if (world.addPlaceable(new Cleaner(game, world, pos[0], pos[1], premium))) {
                        selectedPlaceableType=null;
                        staffShopVisible=true;
                    }
                }else {
                    if (world.addPlaceable(new Building(game, selectedPlaceableType, pos[0], pos[1]))) {
                        selectedPlaceableType = null;
                        shopVisible = true;
                    }
                }
            }
        }
        // none of the above, and edit is open
        else if(editVisible){
            // if moving some item
            if(selectedPlaceableType!=null){
                if(x>=0 && x<=Gdx.graphics.getWidth() && y>=0&& y<=Gdx.graphics.getHeight()){
                    int[] pos = RenderUtils.getPositionFromMouse();

                    if( selectedPlaceableType != null) {
                        // for staff
                        if(selectedPlaceableType == PlaceableType.REPAIRMAN || selectedPlaceableType == PlaceableType.REPAIRMAN_PREMIUM ){
                            boolean premium = selectedPlaceableType == PlaceableType.REPAIRMAN_PREMIUM;
                            if (world.movePlaceable(new Repairman(game, world, pos[0], pos[1], premium))) {
                                selectedPlaceableType=null;
                            }
                        }else if(selectedPlaceableType == PlaceableType.CLEANER || selectedPlaceableType == PlaceableType.CLEANER_PREMIUM ){
                            boolean premium = selectedPlaceableType == PlaceableType.CLEANER_PREMIUM;
                            if (world.movePlaceable(new Cleaner(game, world, pos[0], pos[1], premium))) {
                                selectedPlaceableType=null;
                            }
                        }else { // for buildings
                            if (world.movePlaceable(new Building(game, selectedPlaceableType, pos[0], pos[1]))) {
                                selectedPlaceableType = null;
                            }
                        }
                    }
                }
            }
            // if clicked on delete
            else if(x>=Gdx.graphics.getWidth()/2-180 && x<=Gdx.graphics.getWidth()/2-60 && y>=Gdx.graphics.getHeight()-80 && y<=Gdx.graphics.getHeight()){
                editMode=EditModes.DELETE;
                placeableToEdit = null;
            }
            // if clicked on move
            else if(x>=Gdx.graphics.getWidth()/2-60 && x<=Gdx.graphics.getWidth()/2+60 && y>=Gdx.graphics.getHeight()-80 && y<=Gdx.graphics.getHeight()){
                editMode=EditModes.MOVE;
                placeableToEdit = null;
            }
            // if clicked on price change
            else if(x>=Gdx.graphics.getWidth()/2+60&& x<=Gdx.graphics.getWidth()/2+180 && y>=Gdx.graphics.getHeight()-80 && y<=Gdx.graphics.getHeight()){
                editMode=EditModes.CHANGE_PRICE;
                placeableToEdit = null;
            }
            // if changing the price of some building
            else if(placeableToEdit!=null){
                // if clicked minus
                if(x>=Gdx.graphics.getWidth()/2-100 && x<=Gdx.graphics.getWidth()/2-50 && y>= 0 && y<=60){
                    placeableToEdit.getType().decrementTicketCost();
                }
                //if clicked plus
                if(x>=Gdx.graphics.getWidth()/2+50 && x<=Gdx.graphics.getWidth()/2+100 && y>= 0 && y<=60){
                    placeableToEdit.getType().incrementTicketCost();
                }
            }
            // otherwise (edit buildings)
            else{
                int[] pos = RenderUtils.getPositionFromMouse();
                if(!PositionUtils.samePos(pos, new int[]{11, -11}) && !PositionUtils.samePos(pos, new int[]{12, -11})) {
                    IPlaceable p = world.findPlaceableByPos(pos);
                    if (p != null) {
                        PlaceableType type = p.getType();
                        switch (editMode) {
                            case MOVE:
                                world.removePlaceable(p);
                                selectedPlaceableType = type;
                                break;
                            case DELETE:
                                world.removePlaceable(p);
                                break;
                            case CHANGE_PRICE:
                                if(!p.getType().getCategory().equals("staff") && !p.getType().getCategory().equals("utility"))
                                placeableToEdit = p;
                                break;
                        }
                    }
                }
            }
        }
    }
    /**
     * First Render-Helper function: draws the shop / hire / edit (in-game) buttons
     */
    private void drawButtons(){
        game.batch.begin();
        game.batch.draw(buttonImg, 0f, Gdx.graphics.getHeight()/2f - 120/2f, 40f, 120f);
        game.batch.end();
    }
    /**
     * Sixth Render-Helper function: draws the edit buttons
     */
    private void drawEdit(){
        if(editVisible) {
            game.batch.begin();
            game.batch.draw(editButtonImg, Gdx.graphics.getWidth() / 2f - 360 / 2f, 0, 360f, 80f);
            if(placeableToEdit !=null){
                game.batch.draw(editPriceButtonImg, Gdx.graphics.getWidth() / 2f - 200 / 2f, Gdx.graphics.getHeight()-60, 200f, 60f);
                font.setColor(Color.BLACK);
                font.draw(game.batch, placeableToEdit.getType().getTicketCost()+"$", Gdx.graphics.getWidth()*1f/2+10, Gdx.graphics.getHeight()*0.965f,0,0,false);
            }
            game.batch.end();
        }
    }
    /**
     * Second Render-Helper function: draws the building shop (in-game) menu
     */
    private void drawShop(){
        if(shopVisible) {
            int i=0;
            game.batch.begin();
            game.batch.draw(shopImg, Gdx.graphics.getWidth() * 0.15f, 0, Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.3f);
            switch (shopTab){
                case "games":
                    textures = gamesTextures;
                    break;
                case "shops":
                    textures = shopsTextures;
                    break;
                case "utility":
                    textures = utilityTextures;
                default:
                    break;
            }
            for(Texture t : textures){
                if(t!=null) {
                    // decide what price and ticketCost to display
                    PlaceableType type = PlaceableType.ROLLERCOASTER;
                    if(t == rollercoasterImg){
                        type = PlaceableType.ROLLERCOASTER;
                    }else if(t == bumperCarsImg){
                        type = PlaceableType.BUMPER_CARS;
                    }else if(t == castleImg){
                        type = PlaceableType.CASTLE;
                    }else if(t == carouselImg){
                        type = PlaceableType.CAROUSEL;
                    }else if(t == pendulumImg){
                        type = PlaceableType.PENDULUM;
                    }else if(t == shipImg){
                        type = PlaceableType.SHIP;
                    }else if(t == dropTowerImg){
                        type = PlaceableType.DROP_TOWER;
                    }else if(t == aimImg){
                        type = PlaceableType.AIM;
                    }else if(t == trashcanImg){
                        type = PlaceableType.TRASHCAN;
                    }else if(t == treeImg){
                        type = PlaceableType.TREE;
                    }else if(t == pathImg){
                        type = PlaceableType.PATH;
                    }else if(t == fountainImg){
                        type = PlaceableType.FOUNTAIN;
                    }else if(t == cafeImg){
                        type = PlaceableType.CAFE;
                    }else if(t == stand1Img){
                        type = PlaceableType.STAND1;
                    }else if(t == stand2Img){
                        type = PlaceableType.STAND2;
                    }else if(t == stand3Img){
                        type = PlaceableType.STAND3;
                    }else if(t == stand4Img){
                        type = PlaceableType.STAND4;
                    }
                    // draw the building texture
                    game.batch.draw(t, Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 10, 20, Gdx.graphics.getWidth() * 0.7f / 9f, Gdx.graphics.getHeight() * 0.15f);
                    // draw all the text for prices
                    font.setColor(Color.BLACK);
                    if(type.getCost()!=0){
                        font.draw(game.batch, type.getCost()+"$", Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 78, Gdx.graphics.getHeight() * 0.22f,0,0,false);
                    }
                    if(type.getTicketCost()!=0){
                        font.draw(game.batch, type.getTicketCost()+"$ /visit", Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 85, Gdx.graphics.getHeight() * 0.035f,0,0,false);
                    }
                    i++;
                }
            }
            game.batch.end();
        }
    }
    /**
     * Fifth Render-Helper function: draws the staff shop (in-game) menu
     */
    private void drawStaffShop(){
        if(staffShopVisible) {
            int i=0;
            game.batch.begin();
            game.batch.draw(staffShopImg, Gdx.graphics.getWidth() * 0.15f, 0, Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.3f);
            for(Texture t : staffTextures){
                if(t!=null) {
                    // decide what price and ticketCost to display
                    int price = 0;
                    int range = 0;
                    if(t == repairmanImg){
                        price=100;
                    }else if(t == repairmanGoldImg){
                        price=300;
                    }else if(t == cleanerImg){
                        price=80;
                        range=3;
                    }else if(t == cleanerGoldImg){
                        price=300;
                        range=5;
                    }
                    game.batch.draw(t, Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 10, 20, Gdx.graphics.getWidth() * 0.7f / 9f, Gdx.graphics.getHeight() * 0.15f);
                    // draw all the text for prices
                    font.setColor(Color.BLACK);
                    if(price!=0){
                        font.draw(game.batch, price+"$", Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 78, Gdx.graphics.getHeight() * 0.22f,0,0,false);
                    }
                    if(range!=0){
                        font.draw(game.batch, range+"x"+range, Gdx.graphics.getWidth() * 0.15f + i * Gdx.graphics.getWidth() * 0.7f / 8f + 72, Gdx.graphics.getHeight() * 0.035f,0,0,false);
                    }
                    i++;
                }
            }
            game.batch.end();
        }
    }
    /**
     * Third Render-Helper function: draws the Selected Item at the cursor
     */
    private void drawSelectedItem(float delta){
        if(selectedPlaceableType != null){
            int[] pos = RenderUtils.getPositionFromMouse();
            IPlaceable selectedItem;
            // make a new instance depening on type
            if(selectedPlaceableType == PlaceableType.REPAIRMAN ){
                selectedItem = new Repairman(game, world, pos[0], pos[1], false);
            }else if(selectedPlaceableType == PlaceableType.REPAIRMAN_PREMIUM){
                selectedItem = new Repairman(game, world, pos[0], pos[1], true);
            }else if(selectedPlaceableType == PlaceableType.CLEANER){
                selectedItem = new Cleaner(game, world, pos[0], pos[1], false);
            }else if(selectedPlaceableType == PlaceableType.CLEANER_PREMIUM){
                selectedItem = new Cleaner(game, world, pos[0], pos[1], true);
            }else {
                selectedItem = new Building(game, selectedPlaceableType, pos[0], pos[1]);
                ((Building) selectedItem).initTexture();
            }
            // add selector tiles for overlaps
            for(Distance d : selectedItem.getOverlappingTiles()){
                World.selectedTiles.add(new int[]{pos[0]+d.dx, pos[1]+d.dy});
            }
            // add selector tiles for cleaner range
            if(selectedItem instanceof Cleaner){
                int range = ((Cleaner)selectedItem).getRange();
                for(int i=-1*range; i<range+1; i++){
                    for(int j=-1*range; j<range+1; j++){
                        World.selectedTiles.add(new int[]{pos[0]+i, pos[1]+j});
                    }
                }
            }
            // add selector tiles for trashcan range
            if(selectedItem.getType() == PlaceableType.TRASHCAN){
                int range = 2;
                for(int i=-1*range; i<range+1; i++){
                    for(int j=-1*range; j<range+1; j++){
                        World.selectedTiles.add(new int[]{pos[0]+i, pos[1]+j});
                    }
                }
            }
            // finally draw
            game.batch.begin();
            selectedItem.render(delta);
            game.batch.end();
        }
    }
    /**
     * Fourth Render-Helper function: draws the top menu bar
     */
    private void drawTopBar(){
        game.batch.begin();
        game.batch.draw(topbarImg, Gdx.graphics.getWidth()*0.7f, Gdx.graphics.getHeight()*0.94f, Gdx.graphics.getWidth()*0.3f, Gdx.graphics.getHeight()*0.06f);
        // draw in-game time
        Time.render(game.batch, font, time);
        // draw money
        Economy.render(game.batch, font);
        drawMood();
        game.batch.end();
    }
    /**
     * Writes the Average mood number on the top bar
     */
    private void drawMood(){
        // only update once a second
        if(time%60 == 0) {
            // calculate average mood
            avgMood = 0f;
            for (Guest g : world.guests) {
                avgMood += g.getMood();
            }
            int size = world.guests.size();
            if (size > 0) {
                avgMood = avgMood / size;
            } else {
                // if no guests exist, show average to be 10
                avgMood = 10f;
            }
        }
        // decimal up to 2 digits
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        font.setColor(Color.BLACK);
        font.getData().setScale(1.2f);
        font.draw(game.batch, df.format(avgMood), Gdx.graphics.getWidth() * 0.95f, Gdx.graphics.getHeight() * 0.98f, 0, 0, false);
    }
    /**
     * Memory Cleanup function that is executed in the Screen's respective function
     */
    public void dispose() {
        buttonImg.dispose();
        topbarImg.dispose();
        shopImg.dispose();
        staffShopImg.dispose();
        rollercoasterImg.dispose();
        bumperCarsImg.dispose();
        castleImg.dispose();
        carouselImg.dispose();
        pendulumImg.dispose();
        shipImg.dispose();
        dropTowerImg.dispose();
        aimImg.dispose();
        cafeImg.dispose();
        stand1Img.dispose();
        stand2Img.dispose();
        stand3Img.dispose();
        stand4Img.dispose();
        trashcanImg.dispose();
        treeImg.dispose();
        pathImg.dispose();
        fountainImg.dispose();
        cleanerImg.dispose();
        cleanerGoldImg.dispose();
        repairmanImg.dispose();
        repairmanGoldImg.dispose();
        editButtonImg.dispose();
        editPriceButtonImg.dispose();
    }
}
