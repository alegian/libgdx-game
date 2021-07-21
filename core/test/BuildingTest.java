import com.badlogic.gdx.Game;
import com.gdx.game.HuntersGame;
import com.gdx.game.entities.Building;
import com.gdx.game.entities.Economy;
import com.gdx.game.entities.PlaceableType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildingTest {
    HuntersGame game = new HuntersGame();

    @Test
    public void constructorTest() {
        Building newBuilding = new Building(game, PlaceableType.PENDULUM, 3, 4);
        // check correct type
        assertEquals(PlaceableType.PENDULUM, newBuilding.getType());
        // check position X and Y
        assertEquals(3, newBuilding.getPos()[0]);
        assertEquals(4, newBuilding.getPos()[1]);
    }

    @Test
    public void ticketCostTest() {
        Building newBuilding = new Building(game, PlaceableType.PENDULUM, 3, 4);
        // check correct ticket cost
        assertEquals(PlaceableType.PENDULUM.getTicketCost(), newBuilding.getTicketCost());
    }
}
