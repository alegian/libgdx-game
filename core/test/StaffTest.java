import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.game.HuntersGame;
import com.gdx.game.entities.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {
    HuntersGame game = new HuntersGame();
    World world = new World(game, null);

    @Test
    public void repairmanTest() {
        Repairman newRepairman = new Repairman(game, world, 4, 4, true);
        // check position X and Y
        assertEquals(4, newRepairman.getPlacementPos()[0]);
        assertEquals(4, newRepairman.getPlacementPos()[1]);
        // check if premium
        assertTrue(newRepairman.getPremium());
        // check correct type
        assertEquals(PlaceableType.REPAIRMAN_PREMIUM, newRepairman.getType());
    }
    @Test
    public void cleanerTest() {
        Cleaner newCleaner = new Cleaner(game, world, 2, 2, false);
        // check position X and Y

        // check if premium
        assertFalse(newCleaner.getPremium());
        // check correct type
        assertEquals(PlaceableType.CLEANER, newCleaner.getType());
    }

}
