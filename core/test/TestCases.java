import com.gdx.game.entities.Economy;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(MockitoJunitRunner.class)
class TestCases {

    @Test
    public void test1() {
        //mock(Economy.class);
        Economy eco = new Economy();
        int initialMoney = eco.getDrachmas();
        Economy.addMoney(100);

        assertEquals(initialMoney+100,eco.getDrachmas());
    }
    @Test
    public void test2(){
        Economy eco = new Economy();
        assertEquals(true,Economy.subtractMoney(100));
    }
}