import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.HuntersGame;
import com.gdx.game.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {
    HuntersGame game = new HuntersGame();
    World world = new World(game, null);

    @Test
    public void guestMoodTest() {
        // new guest, should start with mood 10
        Guest guest = new Guest(game, world);
        // decrease mood by 5
        guest.decreaseMood(5);
        // increase mood by 1
        guest.increaseMood(1);
        // check if mood is 6
        assertEquals(6f, guest.getMood(), 0.005f);
    }

    @Test
    public void testUseBuilding(){
        // new guest
        Guest guest = new Guest(game, world);
        // new building target
        Building newBuilding = new Building(game, PlaceableType.PENDULUM, 3, 4);

        // if guest is WALKING and he is at the destination,
        // then next time we should be WAITING
        guest.setPos(3, 4);
        guest.setMode(GuestMode.WALKING);
        guest.setDestinationBuilding(newBuilding);
        guest.setMacroDestination(3,4);
        guest.update(0.1f);

        assertEquals(GuestMode.WAITING, guest.getMode());
    }
}
