import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.HuntersGame;
import com.gdx.game.entities.IPlaceable;
import com.gdx.game.entities.*;
import com.gdx.game.utils.Distance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    HuntersGame game = new HuntersGame();
    World world = new World(game, null);
    public static ArrayList<IPlaceable> Placeable = new ArrayList<>();


    @Test
    public void addPlaceableTest() {
        // reset world
        World.placeables.clear();
        Repairman placeable = new Repairman(game, world, 3, 4, true);
        // check that it has been added
        assertTrue(world.addPlaceable(placeable));
    }
    @Test
    public void addGuestTest() {
        Guest newGuest = new Guest(game, world);
        // when we add a guest
        world.addGuest(newGuest);
        // check that it has been added
        assertTrue(world.getGuests().contains(newGuest));
    }

    @Test
    public void addTrashTest() {
        // original number of trash
        int originalSize = world.getTrash().size();
        // when trash is thrown
        assertTrue(world.addTrash(1, 1));
        // check that it has been added
        assertTrue(world.getTrash().size()>originalSize);
    }

    @Test
    public void findPlaceableTest(){
        // build something at 3,4
        Building newBuilding = new Building(game, PlaceableType.PENDULUM, 3, 4);
        // when we add a placeable
        world.addPlaceable(newBuilding);
        // check that we can find it by pos
        assertEquals(newBuilding, world.findPlaceableByPos(new int[]{3,4}));
    }
}
