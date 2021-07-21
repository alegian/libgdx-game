import com.gdx.game.utils.PositionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionUtilsTest {

    @Test
    void samePos() {
        assertEquals(PositionUtils.samePos(new int[]{ 1,2}, new int[]{ 1,2}),true);
    }
}